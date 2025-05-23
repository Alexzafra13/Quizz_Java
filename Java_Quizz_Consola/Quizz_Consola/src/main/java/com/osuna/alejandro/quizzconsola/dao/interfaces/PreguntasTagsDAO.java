
package com.osuna.alejandro.quizzconsola.dao.interfaces;

import com.osuna.alejandro.quizzconsola.modelos.Preguntas_Tags;
import com.osuna.alejandro.quizzconsola.modelos.Etiquetas;
import java.util.List;

public interface PreguntasTagsDAO {

    boolean insertar(Preguntas_Tags preguntaTag);
    boolean eliminar(Integer preguntaId, Integer etiquetaId);

    List<Etiquetas> obtenerEtiquetasPorPregunta(Integer preguntaId);
    List<Integer> obtenerPreguntasPorEtiqueta(Integer etiquetaId);
}