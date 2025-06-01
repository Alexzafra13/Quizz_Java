package com.osuna.alejandro.quizzconsola;

import com.osuna.alejandro.quizzconsola.controlador_consola.MenuPrincipal;
import com.osuna.alejandro.quizzconsola.util.InicializadorBaseDatos;

public class Main {

    public static void main(String[] args) {

        InicializadorBaseDatos inicializador = new InicializadorBaseDatos();

        MenuPrincipal init = new MenuPrincipal();

        inicializador.inicializarSiEsNecesario();

        init.iniciar();
    }
}