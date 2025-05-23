
package com.osuna.alejandro.quizzconsola.dao.interfaces;

import com.osuna.alejandro.quizzconsola.modelos.Usuario_Respuesta;
import java.util.List;

public interface UsuarioRespuestaDAO {

    Usuario_Respuesta buscarPorId(Integer id);

    boolean insertar(Usuario_Respuesta respuesta);
    boolean actualizar(Usuario_Respuesta respuesta);
    boolean eliminar(Integer id);

    List<Usuario_Respuesta> buscarPorTest(Integer testId, Integer usuarioId);
    List<Usuario_Respuesta> buscarPorUsuario(Integer usuarioId);
}