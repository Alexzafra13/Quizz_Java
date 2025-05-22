package com.osuna.alejandro.quizzconsola.servicio;

import com.osuna.alejandro.quizzconsola.modelos.Usuarios;


public class ServicioAutentificacion {

   static Usuarios user = null;


    public static boolean verificarCredenciales(String pass){

        return user.getPassword_hash().equals(pass);
    }

}
