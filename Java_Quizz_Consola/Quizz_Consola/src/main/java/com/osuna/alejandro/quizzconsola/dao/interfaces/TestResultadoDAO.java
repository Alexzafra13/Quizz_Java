
package com.osuna.alejandro.quizzconsola.dao.interfaces;

import com.osuna.alejandro.quizzconsola.modelos.Test_Resultados;
import java.util.List;

public interface TestResultadoDAO {

    Test_Resultados buscarPorId(Integer id);

    boolean insertar(Test_Resultados resultado);
    boolean actualizar(Test_Resultados resultado);
    boolean eliminar(Integer id);

    List<Test_Resultados> buscarPorUsuario(Integer usuarioId);
    List<Test_Resultados> buscarPorTest(Integer testId);
    Test_Resultados buscarPorUsuarioYTest(Integer usuarioId, Integer testId);
}