package com.osuna.alejandro.quizzconsola.dao.interfaces;

import com.osuna.alejandro.quizzconsola.modelos.Usuarios;
import com.osuna.alejandro.quizzconsola.modelos.enums.Rol;

import java.util.List;

public interface UsuarioDAO {

    Usuarios buscarPorEmail(String email);
    Usuarios buscarPorUsername(String username);
    Usuarios buscarPorId(Integer id);

    //Aqui hare el crud
    boolean insertarUsuario(Usuarios usuario);
    boolean actualizarUsuario(Usuarios usuario);
    boolean eliminarUsuarioId(Integer id);
    boolean eliminarUsuarioNombre(String username);
    boolean eliminarUsuarioEmail(String email);

    // Listas
    List<Usuarios> obtenerTodosLosUsuarios();

}