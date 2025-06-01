package com.osuna.alejandro.quizzconsola.util;

import com.osuna.alejandro.quizzconsola.dao.ConexionBD;
import java.sql.*;

public class InicializadorBaseDatos {

    private ConexionBD conexionBD;

    public InicializadorBaseDatos() {
        this.conexionBD = new ConexionBD();
    }

    public void inicializarSiEsNecesario() {
        try {
            Connection conn = conexionBD.getConexion();
            Statement stmt = conn.createStatement();

            // Hago una consulta para ver si existe la base de datos y si existe no la creo
            try {
                ResultSet rs = stmt.executeQuery("SELECT 1 FROM categorias LIMIT 1");

                rs.close();

                System.out.println("Base de datos ya inicializada.");

            } catch (SQLException e) {

                System.out.println("Creando tablas...");
                crearTablas();
                crearVistas();
                crearProcedimientos();
                crearFunciones();
                crearTriggers();
                insertarDatosIniciales();

                System.out.println("Base de datos lista!");
            }

            stmt.close();

        } catch (Exception e) {

            System.err.println("Error: " + e.getMessage());
        }
    }

    private void crearTablas() throws SQLException {

        Connection conn = conexionBD.getConexion();
        Statement stmt = conn.createStatement();


        stmt.execute("""
            CREATE TABLE categorias (
                id INT PRIMARY KEY AUTO_INCREMENT,
                categoria VARCHAR(100) NOT NULL,
                descripcion TEXT
            )
        """);


        stmt.execute("""
            CREATE TABLE preguntas (
                id INT PRIMARY KEY AUTO_INCREMENT,
                preguntas_text TEXT NOT NULL,
                categoria_id INT NOT NULL,
                dificultad ENUM('Facil', 'Intermedio', 'Dificil'),
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (categoria_id) REFERENCES categorias(id)
            )
        """);


        stmt.execute("""
            CREATE TABLE etiquetas (
                id INT PRIMARY KEY AUTO_INCREMENT,
                etiqueta VARCHAR(50) NOT NULL
            )
        """);


        stmt.execute("""
            CREATE TABLE preguntas_tags (
                id INT PRIMARY KEY AUTO_INCREMENT,
                pregunta_id INT NOT NULL,
                etiqueta_id INT NOT NULL,
                FOREIGN KEY (pregunta_id) REFERENCES preguntas(id),
                FOREIGN KEY (etiqueta_id) REFERENCES etiquetas(id),
                UNIQUE KEY unique_pregunta_etiqueta (pregunta_id, etiqueta_id)
            )
        """);


        stmt.execute("""
            CREATE TABLE opciones (
                id INT PRIMARY KEY AUTO_INCREMENT,
                pregunta_id INT NOT NULL,
                opcion_text TEXT NOT NULL,
                is_correcta BOOLEAN NOT NULL DEFAULT FALSE,
                FOREIGN KEY (pregunta_id) REFERENCES preguntas(id)
            )
        """);


        stmt.execute("""
            CREATE TABLE usuarios (
                id INT PRIMARY KEY AUTO_INCREMENT,
                username VARCHAR(50) NOT NULL,
                email VARCHAR(100) NOT NULL UNIQUE,
                password_hash VARCHAR(255) NOT NULL,
                rol ENUM('Admin', 'Profesor', 'Alumno') DEFAULT 'Alumno',
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """);


        stmt.execute("""
            CREATE TABLE test (
                id INT PRIMARY KEY AUTO_INCREMENT,
                titulo VARCHAR(255) NOT NULL,
                descripcion TEXT,
                created_by INT NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (created_by) REFERENCES usuarios(id)
            )
        """);


        stmt.execute("""
            CREATE TABLE test_preguntas (
                id INT PRIMARY KEY AUTO_INCREMENT,
                test_id INT NOT NULL,
                pregunta_id INT NOT NULL,
                FOREIGN KEY (test_id) REFERENCES test(id),
                FOREIGN KEY (pregunta_id) REFERENCES preguntas(id),
                UNIQUE KEY unique_test_pregunta (test_id, pregunta_id)
            )
        """);


        stmt.execute("""
            CREATE TABLE test_resultados (
                id INT PRIMARY KEY AUTO_INCREMENT,
                usuario_id INT NOT NULL,
                test_id INT NOT NULL,
                puntuacion DECIMAL(5,2) NOT NULL DEFAULT 0,
                inicio DATETIME NOT NULL,
                fin DATETIME,
                FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
                FOREIGN KEY (test_id) REFERENCES test(id)
            )
        """);


        stmt.execute("""
            CREATE TABLE usuario_respuesta (
                id INT PRIMARY KEY AUTO_INCREMENT,
                usuario_id INT NOT NULL,
                test_id INT NOT NULL,
                pregunta_id INT NOT NULL,
                seleccionada_opcion_id INT,
                tiempo_respuesta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
                FOREIGN KEY (test_id) REFERENCES test(id),
                FOREIGN KEY (pregunta_id) REFERENCES preguntas(id),
                FOREIGN KEY (seleccionada_opcion_id) REFERENCES opciones(id)
            )
        """);

        stmt.close();
    }

    private void crearVistas() throws SQLException {
        Connection conn = conexionBD.getConexion();
        Statement stmt = conn.createStatement();

        // Vista 1
        stmt.execute("""
            CREATE VIEW vista_estadisticas_usuario AS
            SELECT 
                usuarios.id,
                usuarios.username,
                usuarios.email,
                usuarios.rol,
                COUNT(DISTINCT test_resultados.test_id) AS tests_realizados,
                AVG(test_resultados.puntuacion) AS puntuacion_media,
                MAX(test_resultados.puntuacion) AS puntuacion_maxima,
                MIN(test_resultados.puntuacion) AS puntuacion_minima,
                SUM(CASE WHEN test_resultados.puntuacion >= 5 THEN 1 ELSE 0 END) AS tests_aprobados,
                SUM(CASE WHEN test_resultados.puntuacion < 5 THEN 1 ELSE 0 END) AS tests_suspendidos
            FROM usuarios
            LEFT JOIN test_resultados ON usuarios.id = test_resultados.usuario_id
            GROUP BY usuarios.id, usuarios.username, usuarios.email, usuarios.rol
        """);

        // Vista 2
        stmt.execute("""
            CREATE VIEW vista_estadisticas_preguntas AS
            SELECT 
                preguntas.id AS pregunta_id,
                preguntas.preguntas_text,
                categorias.categoria,
                preguntas.dificultad,
                COUNT(DISTINCT test_preguntas.test_id) AS numero_tests_incluida,
                COUNT(DISTINCT usuario_respuesta.usuario_id) AS usuarios_que_respondieron,
                COUNT(usuario_respuesta.id) AS total_respuestas
            FROM preguntas
            INNER JOIN categorias ON preguntas.categoria_id = categorias.id
            LEFT JOIN test_preguntas ON preguntas.id = test_preguntas.pregunta_id
            LEFT JOIN usuario_respuesta ON preguntas.id = usuario_respuesta.pregunta_id
            GROUP BY preguntas.id, preguntas.preguntas_text, categorias.categoria, preguntas.dificultad
            ORDER BY total_respuestas DESC
        """);

        stmt.close();
    }

    private void crearProcedimientos() throws SQLException {
        Connection conn = conexionBD.getConexion();
        Statement stmt = conn.createStatement();


        stmt.execute("""
            CREATE PROCEDURE generar_test_aleatorio_simple(
                IN p_titulo VARCHAR(255),
                IN p_created_by INT,
                IN p_num_preguntas INT,
                OUT p_test_id INT
            )
            BEGIN
                DECLARE v_pregunta_id INT;
                DECLARE continuar INT DEFAULT TRUE;
                
                DECLARE cur_preguntas CURSOR FOR
                    SELECT id 
                    FROM preguntas 
                    ORDER BY RAND()
                    LIMIT p_num_preguntas;
                
                DECLARE CONTINUE HANDLER FOR NOT FOUND SET continuar = FALSE;
                
                START TRANSACTION;
                
                INSERT INTO test (titulo, descripcion, created_by, created_at)
                VALUES (p_titulo, 'Test generado automáticamente', p_created_by, NOW());
                
                SET p_test_id = LAST_INSERT_ID();
                
                OPEN cur_preguntas;
                
                FETCH cur_preguntas INTO v_pregunta_id;
                
                WHILE continuar DO
                    INSERT INTO test_preguntas (test_id, pregunta_id)
                    VALUES (p_test_id, v_pregunta_id);
                    
                    FETCH cur_preguntas INTO v_pregunta_id;
                END WHILE;
                
                CLOSE cur_preguntas;
                
                COMMIT;
            END
        """);


        stmt.execute("""
            CREATE PROCEDURE registrar_usuario_seguro(
                IN p_username VARCHAR(100),
                IN p_email VARCHAR(100),
                IN p_password VARCHAR(255),
                IN p_rol VARCHAR(20),
                OUT p_usuario_id INT,
                OUT p_mensaje VARCHAR(255)
            )
            BEGIN
                DECLARE usuario_existe INT DEFAULT 0;
                DECLARE email_existe INT DEFAULT 0;
                
                DECLARE EXIT HANDLER FOR SQLEXCEPTION
                BEGIN
                    ROLLBACK;
                    SET p_mensaje = 'Error: No se pudo registrar el usuario';
                    SET p_usuario_id = NULL;
                END;
                
                START TRANSACTION;
                
                SELECT COUNT(*) INTO usuario_existe 
                FROM usuarios 
                WHERE username = p_username;
                
                SELECT COUNT(*) INTO email_existe 
                FROM usuarios 
                WHERE email = p_email;
                
                IF usuario_existe > 0 THEN
                    ROLLBACK;
                    SET p_mensaje = 'Error: El nombre de usuario ya existe';
                    SET p_usuario_id = NULL;
                ELSEIF email_existe > 0 THEN
                    ROLLBACK;
                    SET p_mensaje = 'Error: El email ya está registrado';
                    SET p_usuario_id = NULL;
                ELSE
                    INSERT INTO usuarios (username, email, password_hash, rol, created_at)
                    VALUES (p_username, p_email, p_password, p_rol, NOW());
                    
                    SET p_usuario_id = LAST_INSERT_ID();
                    
                    COMMIT;
                    SET p_mensaje = 'Usuario registrado correctamente';
                END IF;
            END
        """);

        stmt.close();
    }

    private void crearFunciones() throws SQLException {
        Connection conn = conexionBD.getConexion();
        Statement stmt = conn.createStatement();

        stmt.execute("""
            CREATE FUNCTION tiempo_promedio_test(p_usuario_id INT)
            RETURNS INT
            DETERMINISTIC
            BEGIN
                DECLARE promedio_minutos INT DEFAULT 0;
                
                SELECT AVG(TIMESTAMPDIFF(MINUTE, inicio, fin)) INTO promedio_minutos
                FROM test_resultados
                WHERE usuario_id = p_usuario_id
                AND fin IS NOT NULL
                AND inicio IS NOT NULL;
                
                IF promedio_minutos IS NULL THEN
                    SET promedio_minutos = 0;
                END IF;
                
                RETURN promedio_minutos;
            END
        """);


        stmt.execute("""
            CREATE FUNCTION mejor_puntuacion_usuario(p_usuario_id INT)
            RETURNS DECIMAL(5,2)
            DETERMINISTIC
            BEGIN
                DECLARE mejor_nota DECIMAL(5,2) DEFAULT 0.00;
                
                SELECT MAX(puntuacion) INTO mejor_nota
                FROM test_resultados
                WHERE usuario_id = p_usuario_id;
                
                IF mejor_nota IS NULL THEN
                    SET mejor_nota = 0.00;
                END IF;
                
                RETURN mejor_nota;
            END
        """);

        stmt.close();
    }

    private void crearTriggers() throws SQLException {
        Connection conn = conexionBD.getConexion();
        Statement stmt = conn.createStatement();

        // Trigger 1
        stmt.execute("""
            CREATE TRIGGER prevenir_eliminar_categoria
            BEFORE DELETE ON categorias
            FOR EACH ROW
            BEGIN
                DECLARE total_preguntas INT;
                
                SELECT COUNT(*) INTO total_preguntas
                FROM preguntas
                WHERE categoria_id = OLD.id;
                
                IF total_preguntas > 0 THEN
                    SIGNAL SQLSTATE '45000';
                END IF;
            END
        """);

        // Trigger - primero creo tabla de auditoría
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS auditoria_usuarios (
                id INT AUTO_INCREMENT PRIMARY KEY,
                usuario_id INT,
                campo_modificado VARCHAR(50),
                valor_anterior VARCHAR(255),
                valor_nuevo VARCHAR(255),
                fecha_cambio DATETIME DEFAULT NOW()
            )
        """);

        stmt.execute("""
            CREATE TRIGGER auditoria_usuarios
            AFTER UPDATE ON usuarios
            FOR EACH ROW
            BEGIN
                IF OLD.rol != NEW.rol THEN
                    INSERT INTO auditoria_usuarios (usuario_id, campo_modificado, valor_anterior, valor_nuevo)
                    VALUES (NEW.id, 'rol', OLD.rol, NEW.rol);
                END IF;
                
                IF OLD.email != NEW.email THEN
                    INSERT INTO auditoria_usuarios (usuario_id, campo_modificado, valor_anterior, valor_nuevo)
                    VALUES (NEW.id, 'email', OLD.email, NEW.email);
                END IF;
            END
        """);

        stmt.close();
    }

    private void insertarDatosIniciales() throws SQLException {
        Connection conn = conexionBD.getConexion();
        Statement stmt = conn.createStatement();

        // Verificar si ya hay datos
        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM usuarios");
        rs.next();
        if (rs.getInt(1) > 0) {
            System.out.println("Ya existen datos en la base de datos.");
            return;
        }

        System.out.println("Insertando datos iniciales...");

        // Usuario administrador por defecto
        stmt.executeUpdate("""
        INSERT INTO usuarios (username, email, password_hash, rol) VALUES
        ('admin', 'admin@quizz.com', 'admin', 'Admin')
    """);

        // Usuarios de prueba
        stmt.executeUpdate("""
        INSERT INTO usuarios (username, email, password_hash, rol) VALUES
        ('profesor1', 'profesor1@quizz.com', 'profesor1', 'Profesor'),
        ('alumno1', 'alumno1@quizz.com', 'alumno1', 'Alumno'),
        ('alumno2', 'alumno2@quizz.com', 'alumno2', 'Alumno')
    """);

        // Categorías
        stmt.executeUpdate("""
        INSERT INTO categorias (categoria, descripcion) VALUES
        ('Matemáticas', 'Preguntas sobre matemáticas básicas y avanzadas'),
        ('Historia', 'Preguntas sobre historia universal y de España'),
        ('Ciencias', 'Preguntas de física, química y biología'),
        ('Programación', 'Preguntas sobre desarrollo de software y programación'),
        ('Geografía', 'Preguntas sobre geografía mundial')
    """);

        // Etiquetas
        stmt.executeUpdate("""
        INSERT INTO etiquetas (etiqueta) VALUES
        ('Básico'),
        ('Avanzado'),
        ('Examen'),
        ('Repaso'),
        ('Java')
    """);

        //  Preguntas de ejemplo
        // Matemáticas
        stmt.executeUpdate("""
        INSERT INTO preguntas (preguntas_text, categoria_id, dificultad) VALUES
        ('¿Cuánto es 2 + 2?', 1, 'Facil'),
        ('¿Cuánto es 15 x 4?', 1, 'Facil'),
        ('¿Cuál es la raíz cuadrada de 144?', 1, 'Intermedio'),
        ('¿Cuánto es 7 x 8?', 1, 'Facil'),
        ('Resuelve: 2x + 5 = 15', 1, 'Intermedio')
    """);

        // Historia
        stmt.executeUpdate("""
        INSERT INTO preguntas (preguntas_text, categoria_id, dificultad) VALUES
        ('¿En qué año llegó Colón a América?', 2, 'Facil'),
        ('¿En qué año comenzó la Segunda Guerra Mundial?', 2, 'Intermedio'),
        ('¿Quién fue el primer presidente de Estados Unidos?', 2, 'Facil'),
        ('¿En qué año cayó el Imperio Romano de Occidente?', 2, 'Dificil'),
        ('¿Qué evento marcó el inicio de la Edad Media?', 2, 'Intermedio')
    """);

        // Ciencias
        stmt.executeUpdate("""
        INSERT INTO preguntas (preguntas_text, categoria_id, dificultad) VALUES
        ('¿Cuál es la fórmula química del agua?', 3, 'Facil'),
        ('¿Cuántos planetas tiene el Sistema Solar?', 3, 'Facil'),
        ('¿Qué es la fotosíntesis?', 3, 'Intermedio'),
        ('¿Cuál es la velocidad de la luz?', 3, 'Dificil'),
        ('¿Qué gas es esencial para la respiración?', 3, 'Facil')
    """);

        // Programación
        stmt.executeUpdate("""
        INSERT INTO preguntas (preguntas_text, categoria_id, dificultad) VALUES
        ('¿Qué es un bucle for?', 4, 'Facil'),
        ('¿Qué es una variable?', 4, 'Facil'),
        ('¿Qué es la herencia en POO?', 4, 'Intermedio'),
        ('¿Qué es un patrón Singleton?', 4, 'Dificil'),
        ('¿Cuál es la diferencia entre una clase y un objeto?', 4, 'Intermedio')
    """);

        // Geografía
        stmt.executeUpdate("""
        INSERT INTO preguntas (preguntas_text, categoria_id, dificultad) VALUES
        ('¿Cuál es la capital de España?', 5, 'Facil'),
        ('¿Cuál es el río más largo del mundo?', 5, 'Intermedio'),
        ('¿En qué continente está Egipto?', 5, 'Facil'),
        ('¿Cuántos continentes hay?', 5, 'Facil'),
        ('¿Cuál es la montaña más alta del mundo?', 5, 'Intermedio')
    """);

        // Opciones para las preguntas
        // Para 2 + 2
        stmt.executeUpdate("""
        INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
        (1, '3', false),
        (1, '4', true),
        (1, '5', false),
        (1, '6', false)
    """);

        // Para 15 x 4
        stmt.executeUpdate("""
        INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
        (2, '45', false),
        (2, '50', false),
        (2, '60', true),
        (2, '65', false)
    """);

        // Para raíz de 144
        stmt.executeUpdate("""
        INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
        (3, '10', false),
        (3, '11', false),
        (3, '12', true),
        (3, '13', false)
    """);

        // Para 7 x 8
        stmt.executeUpdate("""
        INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
        (4, '54', false),
        (4, '56', true),
        (4, '58', false),
        (4, '60', false)
    """);

        // Para 2x + 5 = 15
        stmt.executeUpdate("""
        INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
        (5, 'x = 3', false),
        (5, 'x = 4', false),
        (5, 'x = 5', true),
        (5, 'x = 6', false)
    """);

        // Para Colón
        stmt.executeUpdate("""
        INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
        (6, '1490', false),
        (6, '1491', false),
        (6, '1492', true),
        (6, '1493', false)
    """);

        // Para Segunda Guerra Mundial
        stmt.executeUpdate("""
        INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
        (7, '1938', false),
        (7, '1939', true),
        (7, '1940', false),
        (7, '1941', false)
    """);

        // Para primer presidente USA
        stmt.executeUpdate("""
        INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
        (8, 'Thomas Jefferson', false),
        (8, 'George Washington', true),
        (8, 'Abraham Lincoln', false),
        (8, 'Benjamin Franklin', false)
    """);

        // Para caída Imperio Romano
        stmt.executeUpdate("""
        INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
        (9, '376 d.C.', false),
        (9, '410 d.C.', false),
        (9, '476 d.C.', true),
        (9, '510 d.C.', false)
    """);

        // Para inicio Edad Media
        stmt.executeUpdate("""
        INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
        (10, 'La caída de Constantinopla', false),
        (10, 'La caída del Imperio Romano de Occidente', true),
        (10, 'El descubrimiento de América', false),
        (10, 'La coronación de Carlomagno', false)
    """);

        // Para fórmula del agua
        stmt.executeUpdate("""
        INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
        (11, 'H2O', true),
        (11, 'CO2', false),
        (11, 'O2', false),
        (11, 'H2SO4', false)
    """);

        // Para planetas
        stmt.executeUpdate("""
        INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
        (12, '7', false),
        (12, '8', true),
        (12, '9', false),
        (12, '10', false)
    """);

        // Para fotosíntesis
        stmt.executeUpdate("""
        INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
        (13, 'Proceso de respiración de las plantas', false),
        (13, 'Proceso de reproducción de las plantas', false),
        (13, 'Proceso por el cual las plantas producen su alimento usando luz solar', true),
        (13, 'Proceso de crecimiento de las plantas', false)
    """);

        // Para velocidad de la luz
        stmt.executeUpdate("""
        INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
        (14, '150,000 km/s', false),
        (14, '200,000 km/s', false),
        (14, '300,000 km/s', true),
        (14, '400,000 km/s', false)
    """);

        // Para gas respiración
        stmt.executeUpdate("""
        INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
        (15, 'Nitrógeno', false),
        (15, 'Oxígeno', true),
        (15, 'Dióxido de carbono', false),
        (15, 'Hidrógeno', false)
    """);

        // Para bucle for
        stmt.executeUpdate("""
        INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
        (16, 'Una variable', false),
        (16, 'Una estructura de control repetitiva', true),
        (16, 'Una función', false),
        (16, 'Un tipo de dato', false)
    """);

        // Para variable
        stmt.executeUpdate("""
        INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
        (17, 'Un tipo de bucle', false),
        (17, 'Un espacio en memoria para almacenar datos', true),
        (17, 'Una función matemática', false),
        (17, 'Un programa', false)
    """);

        // Para herencia POO
        stmt.executeUpdate("""
        INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
        (18, 'Copiar código de una clase a otra', false),
        (18, 'Mecanismo por el cual una clase adquiere propiedades de otra', true),
        (18, 'Crear múltiples objetos', false),
        (18, 'Eliminar una clase', false)
    """);

        // Para Singleton
        stmt.executeUpdate("""
        INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
        (19, 'Patrón que permite crear múltiples instancias', false),
        (19, 'Patrón que garantiza una única instancia de una clase', true),
        (19, 'Patrón para crear interfaces', false),
        (19, 'Patrón para heredar clases', false)
    """);

        // Para clase vs objeto
        stmt.executeUpdate("""
        INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
        (20, 'No hay diferencia', false),
        (20, 'La clase es el plano y el objeto es la instancia', true),
        (20, 'El objeto es el plano y la clase es la instancia', false),
        (20, 'Ambos son lo mismo', false)
    """);

        // Para capital de España
        stmt.executeUpdate("""
        INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
        (21, 'Barcelona', false),
        (21, 'Madrid', true),
        (21, 'Valencia', false),
        (21, 'Sevilla', false)
    """);

        // Para río más largo
        stmt.executeUpdate("""
        INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
        (22, 'Nilo', false),
        (22, 'Amazonas', true),
        (22, 'Yangtsé', false),
        (22, 'Misisipi', false)
    """);

        // Para Egipto continente
        stmt.executeUpdate("""
        INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
        (23, 'Asia', false),
        (23, 'África', true),
        (23, 'Europa', false),
        (23, 'Oceanía', false)
    """);

        // Para número de continentes
        stmt.executeUpdate("""
        INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
        (24, '5', false),
        (24, '6', false),
        (24, '7', true),
        (24, '8', false)
    """);

        // Para montaña más alta
        stmt.executeUpdate("""
        INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
        (25, 'K2', false),
        (25, 'Monte Everest', true),
        (25, 'Kilimanjaro', false),
        (25, 'Mont Blanc', false)
    """);

        // 7. Creo algunos tests de ejemplo (created_by = 2 es profesor1)
        stmt.executeUpdate("""
        INSERT INTO test (titulo, descripcion, created_by) VALUES
        ('Test de Matemáticas Básicas', 'Test para evaluar conocimientos básicos de matemáticas', 2),
        ('Test de Historia Universal', 'Preguntas sobre historia mundial', 2),
        ('Test de Programación Java', 'Evaluación de conceptos básicos de Java', 2),
        ('Test Mixto Fácil', 'Preguntas fáciles de todas las categorías', 2)
    """);

        // Asignar preguntas a los tests
        // Test 1: Matemáticas (preguntas 1-5)
        stmt.executeUpdate("""
        INSERT INTO test_preguntas (test_id, pregunta_id) VALUES
        (1, 1), (1, 2), (1, 3), (1, 4), (1, 5)
    """);

        // Test 2: Historia (preguntas 6-10)
        stmt.executeUpdate("""
        INSERT INTO test_preguntas (test_id, pregunta_id) VALUES
        (2, 6), (2, 7), (2, 8), (2, 9), (2, 10)
    """);

        // Test 3: Programación (preguntas 16-20)
        stmt.executeUpdate("""
        INSERT INTO test_preguntas (test_id, pregunta_id) VALUES
        (3, 16), (3, 17), (3, 18), (3, 19), (3, 20)
    """);

        // Test 4: Mixto
        stmt.executeUpdate("""
        INSERT INTO test_preguntas (test_id, pregunta_id) VALUES
        (4, 1), (4, 6), (4, 11), (4, 16), (4, 21)
    """);

        stmt.close();
        System.out.println("Datos iniciales insertados correctamente!");
    }

}