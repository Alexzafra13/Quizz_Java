
package com.osuna.alejandro.quizzconsola.dao.interfaces;

import com.osuna.alejandro.quizzconsola.modelos.Categorias;
import java.util.List;

public interface CategoriaDAO {

    Categorias buscarPorId(Integer id);
    Categorias buscarPorNombre(String categoria);

    boolean insertar(Categorias categoria);
    boolean actualizar(Categorias categoria);
    boolean eliminar(Integer id);

    List<Categorias> obtenerTodas();
}