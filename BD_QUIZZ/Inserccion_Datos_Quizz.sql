-- Usuario administrador por defecto
INSERT INTO usuarios (username, email, password_hash, rol) VALUES
('admin', 'admin@quizz.com', 'admin', 'Admin');

-- Usuarios de prueba
INSERT INTO usuarios (username, email, password_hash, rol) VALUES
('profesor1', 'profesor1@quizz.com', 'profesor1', 'Profesor'),
('alumno1', 'alumno1@quizz.com', 'alumno1', 'Alumno'),
('alumno2', 'alumno2@quizz.com', 'alumno2', 'Alumno');

-- Categorías
INSERT INTO categorias (categoria, descripcion) VALUES
('Matemáticas', 'Preguntas sobre matemáticas básicas y avanzadas'),
('Historia', 'Preguntas sobre historia universal y de España'),
('Ciencias', 'Preguntas de física, química y biología'),
('Programación', 'Preguntas sobre desarrollo de software y programación'),
('Geografía', 'Preguntas sobre geografía mundial');

-- Etiquetas
INSERT INTO etiquetas (etiqueta) VALUES
('Básico'),
('Avanzado'),
('Examen'),
('Repaso'),
('Java');

--  Preguntas de ejemplo
-- Matemáticas
INSERT INTO preguntas (preguntas_text, categoria_id, dificultad) VALUES
('¿Cuánto es 2 + 2?', 1, 'Fácil'),
('¿Cuánto es 15 x 4?', 1, 'Fácil'),
('¿Cuál es la raíz cuadrada de 144?', 1, 'Intermedio'),
('¿Cuánto es 7 x 8?', 1, 'Fácil'),
('Resuelve: 2x + 5 = 15', 1, 'Intermedio');

-- Historia 
INSERT INTO preguntas (preguntas_text, categoria_id, dificultad) VALUES
('¿En qué año llegó Colón a América?', 2, 'Fácil'),
('¿En qué año comenzó la Segunda Guerra Mundial?', 2, 'Intermedio'),
('¿Quién fue el primer presidente de Estados Unidos?', 2, 'Fácil'),
('¿En qué año cayó el Imperio Romano de Occidente?', 2, 'Difícil'),
('¿Qué evento marcó el inicio de la Edad Media?', 2, 'Intermedio');

-- Ciencias
INSERT INTO preguntas (preguntas_text, categoria_id, dificultad) VALUES
('¿Cuál es la fórmula química del agua?', 3, 'Fácil'),
('¿Cuántos planetas tiene el Sistema Solar?', 3, 'Fácil'),
('¿Qué es la fotosíntesis?', 3, 'Intermedio'),
('¿Cuál es la velocidad de la luz?', 3, 'Difícil'),
('¿Qué gas es esencial para la respiración?', 3, 'Fácil');

-- Programación
INSERT INTO preguntas (preguntas_text, categoria_id, dificultad) VALUES
('¿Qué es un bucle for?', 4, 'Fácil'),
('¿Qué es una variable?', 4, 'Fácil'),
('¿Qué es la herencia en POO?', 4, 'Intermedio'),
('¿Qué es un patrón Singleton?', 4, 'Difícil'),
('¿Cuál es la diferencia entre una clase y un objeto?', 4, 'Intermedio');

-- Geografía
INSERT INTO preguntas (preguntas_text, categoria_id, dificultad) VALUES
('¿Cuál es la capital de España?', 5, 'Fácil'),
('¿Cuál es el río más largo del mundo?', 5, 'Intermedio'),
('¿En qué continente está Egipto?', 5, 'Fácil'),
('¿Cuántos continentes hay?', 5, 'Fácil'),
('¿Cuál es la montaña más alta del mundo?', 5, 'Intermedio');

-- Opciones para las preguntas
-- Para 2 + 2
INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
(1, '3', false),
(1, '4', true),
(1, '5', false),
(1, '6', false);

-- Para 15 x 4
INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
(2, '45', false),
(2, '50', false),
(2, '60', true),
(2, '65', false);

-- Para raíz de 144
INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
(3, '10', false),
(3, '11', false),
(3, '12', true),
(3, '13', false);

-- Para 7 x 8
INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
(4, '54', false),
(4, '56', true),
(4, '58', false),
(4, '60', false);

-- Para 2x + 5 = 15
INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
(5, 'x = 3', false),
(5, 'x = 4', false),
(5, 'x = 5', true),
(5, 'x = 6', false);

-- Para Colón
INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
(6, '1490', false),
(6, '1491', false),
(6, '1492', true),
(6, '1493', false);

-- Para Segunda Guerra Mundial
INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
(7, '1938', false),
(7, '1939', true),
(7, '1940', false),
(7, '1941', false);

-- Para primer presidente USA
INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
(8, 'Thomas Jefferson', false),
(8, 'George Washington', true),
(8, 'Abraham Lincoln', false),
(8, 'Benjamin Franklin', false);

-- Para caída Imperio Romano
INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
(9, '376 d.C.', false),
(9, '410 d.C.', false),
(9, '476 d.C.', true),
(9, '510 d.C.', false);

-- Para inicio Edad Media
INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
(10, 'La caída de Constantinopla', false),
(10, 'La caída del Imperio Romano de Occidente', true),
(10, 'El descubrimiento de América', false),
(10, 'La coronación de Carlomagno', false);

-- Para fórmula del agua
INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
(11, 'H2O', true),
(11, 'CO2', false),
(11, 'O2', false),
(11, 'H2SO4', false);

-- Para planetas
INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
(12, '7', false),
(12, '8', true),
(12, '9', false),
(12, '10', false);

-- Para fotosíntesis
INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
(13, 'Proceso de respiración de las plantas', false),
(13, 'Proceso de reproducción de las plantas', false),
(13, 'Proceso por el cual las plantas producen su alimento usando luz solar', true),
(13, 'Proceso de crecimiento de las plantas', false);

-- Para velocidad de la luz
INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
(14, '150,000 km/s', false),
(14, '200,000 km/s', false),
(14, '300,000 km/s', true),
(14, '400,000 km/s', false);

-- Para gas respiración
INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
(15, 'Nitrógeno', false),
(15, 'Oxígeno', true),
(15, 'Dióxido de carbono', false),
(15, 'Hidrógeno', false);

-- Para bucle for
INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
(16, 'Una variable', false),
(16, 'Una estructura de control repetitiva', true),
(16, 'Una función', false),
(16, 'Un tipo de dato', false);

-- Para variable
INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
(17, 'Un tipo de bucle', false),
(17, 'Un espacio en memoria para almacenar datos', true),
(17, 'Una función matemática', false),
(17, 'Un programa', false);

-- Para herencia POO
INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
(18, 'Copiar código de una clase a otra', false),
(18, 'Mecanismo por el cual una clase adquiere propiedades de otra', true),
(18, 'Crear múltiples objetos', false),
(18, 'Eliminar una clase', false);

-- Para Singleton
INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
(19, 'Patrón que permite crear múltiples instancias', false),
(19, 'Patrón que garantiza una única instancia de una clase', true),
(19, 'Patrón para crear interfaces', false),
(19, 'Patrón para heredar clases', false);

-- Para clase vs objeto
INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
(20, 'No hay diferencia', false),
(20, 'La clase es el plano y el objeto es la instancia', true),
(20, 'El objeto es el plano y la clase es la instancia', false),
(20, 'Ambos son lo mismo', false);

-- Para capital de España
INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
(21, 'Barcelona', false),
(21, 'Madrid', true),
(21, 'Valencia', false),
(21, 'Sevilla', false);

-- Para río más largo
INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
(22, 'Nilo', false),
(22, 'Amazonas', true),
(22, 'Yangtsé', false),
(22, 'Misisipi', false);

-- Para Egipto continente
INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
(23, 'Asia', false),
(23, 'África', true),
(23, 'Europa', false),
(23, 'Oceanía', false);

-- Para número de continentes
INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
(24, '5', false),
(24, '6', false),
(24, '7', true),
(24, '8', false);

-- Para montaña más alta
INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES
(25, 'K2', false),
(25, 'Monte Everest', true),
(25, 'Kilimanjaro', false),
(25, 'Mont Blanc', false);

-- 7. Creo algunos tests de ejemplo (created_by = 2 es profesor1)
INSERT INTO test (titulo, descripcion, created_by) VALUES
('Test de Matemáticas Básicas', 'Test para evaluar conocimientos básicos de matemáticas', 2),
('Test de Historia Universal', 'Preguntas sobre historia mundial', 2),
('Test de Programación Java', 'Evaluación de conceptos básicos de Java', 2),
('Test Mixto Fácil', 'Preguntas fáciles de todas las categorías', 2);

-- Asignar preguntas a los tests
-- Test 1: Matemáticas (preguntas 1-5)
INSERT INTO test_preguntas (test_id, pregunta_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5);

-- Test 2: Historia (preguntas 6-10)
INSERT INTO test_preguntas (test_id, pregunta_id) VALUES
(2, 6), (2, 7), (2, 8), (2, 9), (2, 10);

-- Test 3: Programación (preguntas 16-20)
INSERT INTO test_preguntas (test_id, pregunta_id) VALUES
(3, 16), (3, 17), (3, 18), (3, 19), (3, 20);

-- Test 4: Mixto
INSERT INTO test_preguntas (test_id, pregunta_id) VALUES
(4, 1), (4, 6), (4, 11), (4, 16), (4, 21);
