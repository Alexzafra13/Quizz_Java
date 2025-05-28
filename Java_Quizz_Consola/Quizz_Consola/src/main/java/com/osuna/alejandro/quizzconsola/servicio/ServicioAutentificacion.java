package com.osuna.alejandro.quizzconsola.servicio;

import com.osuna.alejandro.quizzconsola.dao.implementaciones.UsuarioDAOImpl;
import com.osuna.alejandro.quizzconsola.dao.interfaces.UsuarioDAO;
import com.osuna.alejandro.quizzconsola.modelos.Usuarios;
import com.osuna.alejandro.quizzconsola.modelos.enums.Rol;

public class ServicioAutentificacion {

    private static Usuarios usuarioActual = null;
    private static UsuarioDAO usuarioDAO = new UsuarioDAOImpl();

    public static boolean autenticar(String identificador, String password) {

        Usuarios usuario = null;

        if (identificador.contains("@")) {
            usuario = usuarioDAO.buscarPorEmail(identificador);
        } else {
            usuario = usuarioDAO.buscarPorUsername(identificador);
        }

        // Verifico si existe y la contraseña coincide
        if (usuario != null && usuario.getPassword_hash().equals(password)) {
            usuarioActual = usuario;
            return true;
        }

        return false;
    }

    public static void crearUsuarioAdminPorDefecto() {
        Usuarios admin = usuarioDAO.buscarPorUsername("admin");
        if (admin == null) {
            ServicioUsuario servicioUsuario = new ServicioUsuario();
            servicioUsuario.crearUsuario("admin", "admin@quizz.com", "admin", Rol.Admin);
            System.out.println("Usuario admin creado por defecto");
        }
    }

    public static void cerrarSesion() {
        usuarioActual = null;
    }

    public static boolean hayUsuarioAutenticado() {
        return usuarioActual != null;
    }

    public static Usuarios getUsuarioActual() {
        return usuarioActual;
    }

    // Métodos para saber que rol es el usuario que ha iniciado sesion
    public static boolean esProfesor() {
        return usuarioActual != null && usuarioActual.getRole().toString().equals("Profesor");
    }

    public static boolean esAlumno() {
        return usuarioActual != null && usuarioActual.getRole().toString().equals("Alumno");
    }

    public static boolean esAdmin(){
        return usuarioActual != null && usuarioActual.getRole().toString().equals("Admin");
    }
}