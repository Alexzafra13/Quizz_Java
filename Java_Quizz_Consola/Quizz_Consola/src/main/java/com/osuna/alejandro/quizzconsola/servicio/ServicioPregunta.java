package com.osuna.alejandro.quizzconsola.servicio;

import com.osuna.alejandro.quizzconsola.dao.implementaciones.OpcionDAOImpl;
import com.osuna.alejandro.quizzconsola.dao.interfaces.OpcionDAO;
import com.osuna.alejandro.quizzconsola.dao.interfaces.PreguntasDAO;
import com.osuna.alejandro.quizzconsola.modelos.Opciones;
import com.osuna.alejandro.quizzconsola.modelos.Preguntas;
import com.osuna.alejandro.quizzconsola.modelos.enums.Dificultad;

import java.time.LocalDate;
import java.util.List;

public class ServicioPregunta {

    private PreguntasDAO preguntasDAO;
    private OpcionDAO opcionDAO;

    public ServicioPregunta(PreguntasDAO preguntasDAO) {
        this.preguntasDAO = preguntasDAO;
        this.opcionDAO = new OpcionDAOImpl();
    }

    // CRUD
    public boolean crearPregunta(String pregunta_text, Integer categoria_id, Dificultad dificultad) {
        Preguntas nueva_pregunta = new Preguntas(pregunta_text, categoria_id, dificultad, LocalDate.now());

        boolean resultado = preguntasDAO.insertar(nueva_pregunta);

        if (resultado) {
            System.out.println("Pregunta creada correctamente");
        } else {
            System.out.println("Error al crear la pregunta");
        }

        return resultado;
    }

    public Preguntas buscarPorId(Integer id) {
        if (id == null || id <= 0) {
            throw new RuntimeException("ID de pregunta no válido");
        }
        return preguntasDAO.buscarPorId(id);
    }

    public boolean actualizarPregunta(Preguntas pregunta) {
        if (pregunta == null) {
            throw new RuntimeException("La pregunta no puede ser null");
        }

        boolean resultado = preguntasDAO.actualizar(pregunta);

        if (resultado) {
            System.out.println("Pregunta actualizada correctamente");
        } else {
            System.out.println("Error al actualizar la pregunta");
        }

        return resultado;
    }

    public boolean eliminarPregunta(Integer id) {
        if (id == null || id <= 0) {
            throw new RuntimeException("ID de pregunta no válido");
        }

        boolean resultado = preguntasDAO.eliminar(id);

        if (resultado) {
            System.out.println("Pregunta eliminada correctamente");
        } else {
            System.out.println("Error al eliminar la pregunta");
        }

        return resultado;
    }



    public List<Preguntas> obtenerTodasLasPreguntas() {
        return preguntasDAO.obtenerTodas();
    }


    public List<Preguntas> listaPreguntasPorCategoria(Integer catId) {
        if (catId == null || catId <= 0) {
            throw new RuntimeException("ID de categoría no válido");
        }

        return preguntasDAO.buscarPorCategoria(catId);
    }

    public List<Preguntas> listaPreguntasPorDificultad(Dificultad dif) {
        if (dif == null) {
            throw new RuntimeException("La dificultad no puede ser null");
        }

        return preguntasDAO.buscarPorDificultad(dif);
    }

    public List<Preguntas> obtenerPreguntasAleatorias(int cantidad) {
        if (cantidad <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a 0");
        }

        return preguntasDAO.obtenerAleatorias(cantidad);
    }


    public boolean agregarOpcionAPregunta(Integer preguntaId, String texto, boolean esCorrecta) {
        if (preguntaId == null || preguntaId <= 0) {
            throw new RuntimeException("ID de pregunta no válido");
        }

        if (texto == null || texto.trim().isEmpty()) {
            throw new RuntimeException("El texto de la opción no puede estar vacío");
        }

        // Verificar que la pregunta existe
        if (buscarPorId(preguntaId) == null) {
            throw new RuntimeException("La pregunta no existe");
        }

        // Si es correcta, verificar que no haya ya una respuesta correcta
        if (esCorrecta) {
            Opciones respuestaCorrecta = opcionDAO.buscarRespuestaCorrecta(preguntaId);
            if (respuestaCorrecta != null) {
                throw new RuntimeException("La pregunta ya tiene una respuesta correcta");
            }
        }

        Opciones nuevaOpcion = new Opciones(null, preguntaId, texto, esCorrecta);
        boolean resultado = opcionDAO.insertar(nuevaOpcion);

        if (resultado) {
            System.out.println("Opción agregada correctamente");
        } else {
            System.out.println("Error al agregar la opción");
        }

        return resultado;
    }

    public List<Opciones> obtenerOpcionesDePregunta(Integer preguntaId) {
        if (preguntaId == null || preguntaId <= 0) {
            throw new RuntimeException("ID de pregunta no válido");
        }

        return opcionDAO.buscarPorPregunta(preguntaId);
    }

    public Opciones obtenerRespuestaCorrecta(Integer preguntaId) {
        if (preguntaId == null || preguntaId <= 0) {
            throw new RuntimeException("ID de pregunta no válido");
        }

        return opcionDAO.buscarRespuestaCorrecta(preguntaId);
    }

    private boolean validadorPreguntaPorOpciones(Integer preguntaId) {
        if (preguntaId == null || preguntaId <= 0) {
            return false;
        }

        List<Opciones> opciones = opcionDAO.buscarPorPregunta(preguntaId);

        // Verificar que tenga al menos 2 opciones
        if (opciones.size() < 2) {
            System.out.println("La pregunta debe tener al menos 2 opciones");
            return false;
        }

        // Contar respuestas correctas
        long respuestasCorrectas = opciones.stream()
                .filter(Opciones::isIs_correcta)
                .count();

        // Verificar que tiene 1 respuesta correcta
        if (respuestasCorrectas != 1) {
            System.out.println("La pregunta debe tener exactamente 1 respuesta correcta");
            return false;
        }

        return true;
    }
}