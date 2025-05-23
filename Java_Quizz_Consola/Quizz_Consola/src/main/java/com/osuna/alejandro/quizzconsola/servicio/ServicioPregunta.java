package com.osuna.alejandro.quizzconsola.servicio;

import com.osuna.alejandro.quizzconsola.dao.interfaces.PreguntasDAO;
import com.osuna.alejandro.quizzconsola.modelos.Preguntas;
import com.osuna.alejandro.quizzconsola.modelos.enums.Dificultad;

import java.time.LocalDate;
import java.util.List;

public class ServicioPregunta {

   private PreguntasDAO preguntasDAO;

    public ServicioPregunta(PreguntasDAO preguntasDAO) {
        this.preguntasDAO = preguntasDAO;
    }

    public boolean crearPregunta(String preguntas_text, Integer categoria_id, Dificultad dificultad) {

        Preguntas nueva_pregunta = new Preguntas(preguntas_text, categoria_id, dificultad, LocalDate.now());

        boolean resultado = preguntasDAO.insertar(nueva_pregunta);

        if (resultado) {
            System.out.println("Pregunta creada correctamente");
        } else {
            System.out.println("Error al crear la pregunta");
        }

        return resultado;
    }

    public List<Preguntas> listaPreguntasPorCategoria(Integer catId){

        if (catId == null || catId <= 0) {
            throw new RuntimeException("ID de categoría no válido");
        }

        return preguntasDAO.buscarPorCategoria(catId);
    }

    public List<Preguntas> listaPreguntasPorDificultad(Dificultad dif){

        return preguntasDAO.buscarPorDificultad(dif);
    }

    private boolean validadorPreguntaPorOpciones()



}
