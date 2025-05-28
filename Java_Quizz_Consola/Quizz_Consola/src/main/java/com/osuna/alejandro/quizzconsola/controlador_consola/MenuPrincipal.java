package com.osuna.alejandro.quizzconsola.controlador_consola;

import com.osuna.alejandro.quizzconsola.servicio.ServicioAutentificacion;
import com.osuna.alejandro.quizzconsola.servicio.ServicioUsuario;
import com.osuna.alejandro.quizzconsola.modelos.Usuarios;
import com.osuna.alejandro.quizzconsola.modelos.enums.Rol;

import java.util.Scanner;

public class MenuPrincipal {

    private Scanner sc = new Scanner(System.in);

    public void iniciar() {

        // Creo el usuario admin por defecto si no existe
        crearUsuarioAdminPorDefecto();

        System.out.println("=== SISTEMA DE QUIZZ ===");
        System.out.println("Bienvenido, por favor inicie sesión");
        System.out.println("Usuario por defecto: admin / admin");

        boolean autenticado = false;

        while (!autenticado) {

            System.out.print("Usuario/Email: ");
            String identificador = sc.nextLine();

            System.out.print("Contraseña: ");
            String password = sc.nextLine();

            if (ServicioAutentificacion.autenticar(identificador, password)) {
                autenticado = true;
                redirigirSegunRol();
            } else {
                System.out.println("Credenciales incorrectas. Intente de nuevo.\n");
            }
        }
    }

    private void crearUsuarioAdminPorDefecto() {
        try {
            ServicioUsuario servicioUsuario = new ServicioUsuario();

            // Verificar si ya existe admin
            Usuarios admin = servicioUsuario.buscarPorUsername("admin");
            if (admin == null) {
                servicioUsuario.crearUsuario("admin", "admin@quizz.com", "admin", Rol.Admin);
                System.out.println("Usuario admin creado por defecto (admin/admin)");
            }
        } catch (Exception e) {
            System.out.println("Nota: Usar credenciales existentes en la BD");
        }
    }

    private void redirigirSegunRol() {

        Usuarios usuario = ServicioAutentificacion.getUsuarioActual();
        System.out.println("Bienvenido, " + usuario.getUsername() + "!");

        if (ServicioAutentificacion.esAdmin()) {
            new MenuAdmin().mostrarMenuAdmin();
        } else if (ServicioAutentificacion.esProfesor()) {
            new MenuProfesor().mostrarMenuProfesor();
        } else if (ServicioAutentificacion.esAlumno()) {
            new MenuAlumno().mostrarMenuAlumno();
        }

        ServicioAutentificacion.cerrarSesion();
        System.out.println("Sesión cerrada. ¡Hasta luego!");
    }
}