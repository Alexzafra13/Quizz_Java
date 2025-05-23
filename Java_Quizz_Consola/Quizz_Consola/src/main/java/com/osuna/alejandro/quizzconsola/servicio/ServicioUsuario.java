package com.osuna.alejandro.quizzconsola.servicio;

import com.osuna.alejandro.quizzconsola.dao.implementaciones.UsuarioDAOImpl;
import com.osuna.alejandro.quizzconsola.dao.interfaces.UsuarioDAO;
import com.osuna.alejandro.quizzconsola.modelos.Usuarios;
import com.osuna.alejandro.quizzconsola.modelos.enums.Rol;

import java.time.LocalDate;
import java.util.List;

public class ServicioUsuario {

    private UsuarioDAO usuarioDAO;

    public ServicioUsuario() {
        this.usuarioDAO = new UsuarioDAOImpl();
    }

    // Validaciones privadas para usar solo en la clase
    private boolean validarEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    private boolean validarPassword(String password) {
        return password != null && password.length() >= 6;
    }

    private boolean validarUsername(String username) {
        return username != null && username.trim().length() >= 3;
    }

    // CRUD
    public boolean crearUsuario(String username, String email, String password, Rol rol) {
        // Validaciones de negocio
        if (!validarEmail(email)) {
            throw new RuntimeException("Email no válido");
        }

        if (!validarPassword(password)) {
            throw new RuntimeException("Password debe tener al menos 6 caracteres");
        }

        // Crear usuario sin ID (constructor para insertar)
        Usuarios nuevoUsuario = new Usuarios(username, email, password, rol, LocalDate.now());

        return usuarioDAO.insertarUsuario(nuevoUsuario);
    }

    public Usuarios buscarPorId(Integer id) {
        return usuarioDAO.buscarPorId(id);
    }

    public Usuarios buscarPorEmail(String email) {
        return usuarioDAO.buscarPorEmail(email);
    }

    public Usuarios buscarPorUsername(String username) {
        return usuarioDAO.buscarPorUsername(username);
    }

    public boolean actualizarUsuario(Usuarios usuario) {
        return usuarioDAO.actualizarUsuario(usuario);
    }

    public boolean eliminarUsuario(Integer id) {
        return usuarioDAO.eliminarUsuarioId(id);
    }

    public List<Usuarios> obtenerTodosLosUsuarios() {
        return usuarioDAO.obtenerTodosLosUsuarios();
    }


    public List<Usuarios> obtenerAlumnos() {
        return obtenerTodosLosUsuarios().stream()
                .filter(u -> u.getRole() == Rol.Alumno)
                .toList();
    }

    public boolean cambiarPassword(Integer usuarioId, String passwordActual, String nuevaPassword) {
        Usuarios usuario = usuarioDAO.buscarPorId(usuarioId);

        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        if (!usuario.getPassword_hash().equals(passwordActual)) {
            throw new RuntimeException("Password actual incorrecta");
        }

        if (!validarPassword(nuevaPassword)) {
            throw new RuntimeException("Nueva password no válida");
        }

        usuario.setPassword_hash(nuevaPassword);
        return usuarioDAO.actualizarUsuario(usuario);
    }

}