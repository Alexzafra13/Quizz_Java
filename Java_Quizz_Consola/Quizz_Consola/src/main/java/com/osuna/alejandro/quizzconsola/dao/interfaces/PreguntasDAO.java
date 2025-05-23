
package com.osuna.alejandro.quizzconsola.dao.interfaces;

import com.osuna.alejandro.quizzconsola.modelos.Preguntas;
import com.osuna.alejandro.quizzconsola.modelos.enums.Dificultad;
import java.util.List;

public interface PreguntasDAO {

    Preguntas buscarPorId(Integer id);

    boolean insertar(Preguntas pregunta);
    boolean actualizar(Preguntas pregunta);
    boolean eliminar(Integer id);

    List<Preguntas> obtenerTodas();
    List<Preguntas> buscarPorCategoria(Integer categoriaId);
    List<Preguntas> buscarPorDificultad(Dificultad dificultad);
    List<Preguntas> obtenerAleatorias(int cantidad);
}