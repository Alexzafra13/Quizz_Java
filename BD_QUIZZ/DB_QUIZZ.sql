
CREATE DATABASE IF NOT EXISTS QUIZZ;
USE QUIZZ;


CREATE TABLE categorias (
    id INT PRIMARY KEY AUTO_INCREMENT,
    categoria VARCHAR(100) NOT NULL,
    descripcion TEXT
);


CREATE TABLE preguntas (
    id INT PRIMARY KEY AUTO_INCREMENT,
    preguntas_text TEXT NOT NULL,
    categoria_id INT NOT NULL,
    dificultad ENUM('Fácil', 'Intermedio', 'Difícil'),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (categoria_id) REFERENCES categorias(id)
);


CREATE TABLE etiquetas (
    id INT PRIMARY KEY AUTO_INCREMENT,
    etiqueta VARCHAR(50) NOT NULL
);


CREATE TABLE preguntas_tags (
    id INT PRIMARY KEY AUTO_INCREMENT,
    pregunta_id INT NOT NULL,
    etiqueta_id INT NOT NULL,
    FOREIGN KEY (pregunta_id) REFERENCES preguntas(id),
    FOREIGN KEY (etiqueta_id) REFERENCES etiquetas(id),
    UNIQUE KEY unique_pregunta_etiqueta (pregunta_id, etiqueta_id)
);


CREATE TABLE opciones (
    id INT PRIMARY KEY AUTO_INCREMENT,
    pregunta_id INT NOT NULL,
    opcion_text TEXT NOT NULL,
    is_correcta BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (pregunta_id) REFERENCES preguntas(id)
);


CREATE TABLE usuarios (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    rol ENUM('Admin', 'Profesor', 'Alumno') DEFAULT 'Alumno',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE test (
    id INT PRIMARY KEY AUTO_INCREMENT,
    titulo VARCHAR(255) NOT NULL,
    descripcion TEXT,
    created_by INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES usuarios(id)
);


CREATE TABLE test_preguntas (
    id INT PRIMARY KEY AUTO_INCREMENT,
    test_id INT NOT NULL,
    pregunta_id INT NOT NULL,
    FOREIGN KEY (test_id) REFERENCES test(id),
    FOREIGN KEY (pregunta_id) REFERENCES preguntas(id),
    UNIQUE KEY unique_test_pregunta (test_id, pregunta_id)
);


CREATE TABLE test_resultados (
    id INT PRIMARY KEY AUTO_INCREMENT,
    usuario_id INT NOT NULL,
    test_id INT NOT NULL,
    puntuacion DECIMAL(5,2) NOT NULL DEFAULT 0,
    inicio DATETIME NOT NULL,
    fin DATETIME,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    FOREIGN KEY (test_id) REFERENCES test(id)
);


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
);
