-- Insertar usuarios de prueba para cada rol
INSERT INTO usuarios (username, email, password_hash, rol) VALUES
-- Administradores
('admin', 'admin@quizz.com', 'admin123', 'Admin'),
('superadmin', 'superadmin@quizz.com', 'super123', 'Admin'),

-- Profesores
('prof_garcia', 'garcia@quizz.com', 'garcia123', 'Profesor'),
('prof_martinez', 'martinez@quizz.com', 'martinez123', 'Profesor'),
('prof_lopez', 'lopez@quizz.com', 'lopez123', 'Profesor'),

-- Alumnos
('alumno_juan', 'juan@estudiante.com', 'juan123', 'Alumno'),
('alumno_maria', 'maria@estudiante.com', 'maria123', 'Alumno'),
('alumno_carlos', 'carlos@estudiante.com', 'carlos123', 'Alumno'),
('alumno_ana', 'ana@estudiante.com', 'ana123', 'Alumno'),
('alumno_pedro', 'pedro@estudiante.com', 'pedro123', 'Alumno');