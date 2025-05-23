
package com.osuna.alejandro.quizzconsola.dao.interfaces;

import com.osuna.alejandro.quizzconsola.modelos.Opciones;
import java.util.List;

public interface OpcionDAO {

    Opciones buscarPorId(Integer id);

    boolean insertar(Opciones opcion);
    boolean actualizar(Opciones opcion);
    boolean eliminar(Integer id);

    List<Opciones> buscarPorPregunta(Integer preguntaId);
    Opciones buscarRespuestaCorrecta(Integer preguntaId);
    List<Opciones> obtenerTodas();
}