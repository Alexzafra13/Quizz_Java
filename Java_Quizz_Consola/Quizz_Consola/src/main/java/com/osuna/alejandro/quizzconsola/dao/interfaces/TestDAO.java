
package com.osuna.alejandro.quizzconsola.dao.interfaces;

import com.osuna.alejandro.quizzconsola.modelos.Test;
import java.util.List;

public interface TestDAO {

    Test buscarPorId(Integer id);

    boolean insertar(Test test);
    boolean actualizar(Test test);
    boolean eliminar(Integer id);

    List<Test> obtenerTodos();
}