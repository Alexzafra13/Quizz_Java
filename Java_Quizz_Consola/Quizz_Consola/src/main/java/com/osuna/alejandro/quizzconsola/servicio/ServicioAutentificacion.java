package com.osuna.alejandro.quizzconsola.servicio;

import com.osuna.alejandro.quizzconsola.modelos.Usuarios;


public class ServicioAutentificacion {

  private static Usuarios usuarioActual = null;


    public static boolean verificarPorUsuario(String pass,  String usuario){

        return usuarioActual.getUsername().equals(usuario) && usuarioActual.getPassword_hash().equals(pass);

    }

    public static boolean verificarPorEmail(String pass, String email){

        return usuarioActual.getEmail().equals(email) && usuarioActual.getPassword_hash().equals(pass);

    }


    public static Usuarios getUsuarioActual() {
        return usuarioActual;
    }
}
