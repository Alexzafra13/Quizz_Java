
package com.osuna.alejandro.quizzconsola.dao.interfaces;

import com.osuna.alejandro.quizzconsola.modelos.Preguntas;
import java.util.List;

public interface TestPreguntasDAO {

    boolean insertar(Integer testId, Integer preguntaId);
    boolean eliminar(Integer testId, Integer preguntaId);

    List<Preguntas> obtenerPreguntasPorTest(Integer testId);
    List<Integer> obtenerTestsPorPregunta(Integer preguntaId);
}