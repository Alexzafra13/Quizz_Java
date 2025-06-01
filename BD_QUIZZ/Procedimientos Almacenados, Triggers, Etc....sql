
-- 1º Vista que da las estadisticas de test por usuario
CREATE VIEW vista_estadisticas_usuario AS
SELECT 
    usuarios.id,
    usuarios.username,
    usuarios.email,
    usuarios.rol,
    COUNT(DISTINCT test_resultados.test_id) AS tests_realizados,
    COALESCE(AVG(test_resultados.puntuacion), 0) AS puntuacion_media,
    COALESCE(MAX(test_resultados.puntuacion), 0) AS puntuacion_maxima,
    COALESCE(MIN(test_resultados.puntuacion), 0) AS puntuacion_minima,
    SUM(CASE WHEN test_resultados.puntuacion >= 5 THEN 1 ELSE 0 END) AS tests_aprobados,
    SUM(CASE WHEN test_resultados.puntuacion < 5 THEN 1 ELSE 0 END) AS tests_suspendidos
FROM usuarios
LEFT JOIN test_resultados ON usuarios.id = test_resultados.usuario_id
GROUP BY usuarios.id, usuarios.username, usuarios.email, usuarios.rol;

-- 2º Vista que muestra las veces que se respondieron las preguntas
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
ORDER BY total_respuestas DESC;

-- 1º Procedimiento Almacenado con cursor y transaccion que genera un test aleatorio partiendo de preguntas existentes
DELIMITER $$

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
    
END$$

DELIMITER ;

-- 2º Procedimiento Almacenado, este para registrar con exito un usuario y si no que se deshaga

DELIMITER $$

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
    
    -- Verifico si el email ya existe
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
    
END$$

DELIMITER ;


-- 1º Funcion para calcular el tiempo medio que se tarda en hacer el test

DELIMITER $$

CREATE FUNCTION tiempo_promedio_test(p_usuario_id INT)
RETURNS INT
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
    
END$$

DELIMITER ;

-- 2º Funcion para calcular la mejor puntuacion que tiene un usuario en los test

DELIMITER $$

CREATE FUNCTION mejor_puntuacion_usuario(p_usuario_id INT)
RETURNS DECIMAL(5,2)
BEGIN

    DECLARE mejor_nota DECIMAL(5,2) DEFAULT 0.00;
    
    SELECT MAX(puntuacion) INTO mejor_nota
    FROM test_resultados
    WHERE usuario_id = p_usuario_id;
    
    IF mejor_nota IS NULL THEN
        SET mejor_nota = 0.00;
    END IF;
    
    RETURN mejor_nota;
    
END$$

DELIMITER ;


-- 1º Trigger, que impide eliminar una categoria si preguntas de tests forman parte de ella (Para que no queden preguntas sin categorias)

DELIMITER $$

CREATE TRIGGER prevenir_eliminar_categoria
BEFORE DELETE ON categorias
FOR EACH ROW
BEGIN
    DECLARE total_preguntas INT;
    
    -- Contar preguntas de esta categoría
    SELECT COUNT(*) INTO total_preguntas
    FROM preguntas
    WHERE categoria_id = OLD.id;
    
    -- Si tiene preguntas, no permitir eliminar
    IF total_preguntas > 0 THEN
        SIGNAL SQLSTATE '45000';
    END IF;
END$$

DELIMITER ;

-- 2º Trigger, que guarda y audita los cambios que se hacen en las cuentas de usuarios, se registran, y asi saber quien ha cambiado de rol, email, etc...ALTER
DELIMITER $$

CREATE TRIGGER auditoria_usuarios

AFTER UPDATE ON usuarios

FOR EACH ROW

BEGIN

    CREATE TABLE IF NOT EXISTS auditoria_usuarios (
        id INT AUTO_INCREMENT PRIMARY KEY,
        usuario_id INT,
        campo_modificado VARCHAR(50),
        valor_anterior VARCHAR(255),
        valor_nuevo VARCHAR(255),
        fecha_cambio DATETIME DEFAULT NOW()
    );
    
    -- Registrar cambio de rol
    IF OLD.rol != NEW.rol THEN
        INSERT INTO auditoria_usuarios (usuario_id, campo_modificado, valor_anterior, valor_nuevo)
        VALUES (NEW.id, 'rol', OLD.rol, NEW.rol);
    END IF;
    
    -- Registrar cambio de email
    IF OLD.email != NEW.email THEN
        INSERT INTO auditoria_usuarios (usuario_id, campo_modificado, valor_anterior, valor_nuevo)
        VALUES (NEW.id, 'email', OLD.email, NEW.email);
    END IF;
END$$

DELIMITER ;


