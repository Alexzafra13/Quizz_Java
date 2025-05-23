
package com.osuna.alejandro.quizzconsola.dao.interfaces;

import com.osuna.alejandro.quizzconsola.modelos.Etiquetas;
import java.util.List;

public interface EtiquetaDAO {

    Etiquetas buscarPorId(Integer id);
    Etiquetas buscarPorNombre(String etiqueta);

    boolean insertar(Etiquetas etiqueta);
    boolean actualizar(Etiquetas etiqueta);
    boolean eliminar(Integer id);

    List<Etiquetas> obtenerTodas();
}