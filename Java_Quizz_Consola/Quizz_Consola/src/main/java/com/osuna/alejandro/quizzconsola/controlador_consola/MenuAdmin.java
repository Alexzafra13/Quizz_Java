package com.osuna.alejandro.quizzconsola.controlador_consola;

import com.osuna.alejandro.quizzconsola.modelos.Categorias;
import com.osuna.alejandro.quizzconsola.modelos.Usuarios;
import com.osuna.alejandro.quizzconsola.modelos.Test;
import com.osuna.alejandro.quizzconsola.modelos.enums.Rol;
import com.osuna.alejandro.quizzconsola.servicio.*;
import com.osuna.alejandro.quizzconsola.util.ConfiguracionManager;

import java.util.List;
import java.util.Scanner;

public class MenuAdmin {

    private Scanner sc = new Scanner(System.in);
    private ServicioUsuario servicioUsuario;
    private ServicioCategoria servicioCategoria;
    private ServicioTest servicioTest;
    private ServicioPregunta servicioPregunta;
    private ConfiguracionManager configuracion;

    public MenuAdmin() {
        this.servicioUsuario = new ServicioUsuario();
        this.servicioCategoria = new ServicioCategoria();
        this.servicioTest = new ServicioTest();
        this.configuracion = new ConfiguracionManager();
    }

    public void mostrarMenuAdmin() {

        int opcion;
        boolean salir = false;

        do {
            System.out.println("""
                   1. Gestión de usuarios
                   2. Gestión de categorías
                   3. Ver estadísticas básicas
                   4. Configuración global
                   5. Salir
                   Seleccione una opción:""");

            opcion = sc.nextInt();

            switch (opcion) {
                case 1:
                    gestionUsuarios();
                    break;
                case 2:
                    gestionCategorias();
                    break;
                case 3:
                    verEstadisticas();
                    break;
                case 4:
                    configuracionGlobal();
                    break;
                case 5:
                    salir = true;
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida");
            }

        } while (!salir);
    }

    private void gestionUsuarios() {
        System.out.println("""
                1. Ver todos los usuarios
                2. Crear nuevo usuario
                3. Eliminar usuario
                4. Volver al menú principal""");

        System.out.print("Seleccione opción: ");
        int opcion = sc.nextInt();

        switch (opcion) {
            case 1:
                verTodosLosUsuarios();
                break;
            case 2:
                crearNuevoUsuario();
                break;
            case 3:
                eliminarUsuario();
                break;
            case 4:
                return;
        }
    }

    private void verTodosLosUsuarios() {
        List<Usuarios> usuarios = servicioUsuario.obtenerTodosLosUsuarios();

        System.out.println("\n=== TODOS LOS USUARIOS ===");
        for (Usuarios usuario : usuarios) {
            System.out.println("ID: " + usuario.getId() +
                    " | Username: " + usuario.getUsername() +
                    " | Email: " + usuario.getEmail() +
                    " | Rol: " + usuario.getRole());
        }
    }

    private void crearNuevoUsuario() {

        sc.nextLine();

        System.out.print("Username: ");
        String username = sc.nextLine();

        System.out.print("Email: ");
        String email = sc.nextLine();

        System.out.print("Password: ");
        String password = sc.nextLine();

        System.out.println("Rol: 1.Admin 2.Profesor 3.Alumno");
        int rolOpcion = sc.nextInt();

        Rol rol = switch (rolOpcion) {
            case 1 -> Rol.Admin;
            case 2 -> Rol.Profesor;
            case 3 -> Rol.Alumno;
            default -> Rol.Alumno;
        };

        if (servicioUsuario.crearUsuario(username, email, password, rol)) {
            System.out.println("Usuario creado correctamente");
        } else {
            System.out.println("Error al crear usuario");
        }
    }

    private void eliminarUsuario() {
        verTodosLosUsuarios();

        System.out.print("Ingrese ID del usuario a eliminar: ");
        Integer usuarioId = sc.nextInt();

        if (servicioUsuario.eliminarUsuario(usuarioId)) {
            System.out.println("Usuario eliminado correctamente");
        } else {
            System.out.println("Error al eliminar usuario");
        }
    }

    private void gestionCategorias() {
        System.out.println("""
                1. Ver todas las categorías
                2. Crear nueva categoría
                3. Eliminar categoría
                4. Volver al menú principal""");

        System.out.print("Seleccione opción: ");
        int opcion = sc.nextInt();

        switch (opcion) {
            case 1:
                verTodasLasCategorias();
                break;
            case 2:
                crearNuevaCategoria();
                break;
            case 3:
                eliminarCategoria();
                break;
            case 4:
                return;
        }
    }

    private void verTodasLasCategorias() {

        List<Categorias> categorias = servicioCategoria.obtenerTodasLasCategorias();

        System.out.println("\n=== TODAS LAS CATEGORÍAS ===");
        for (Categorias categoria : categorias) {
            System.out.println("ID: " + categoria.getId() +
                    " | Nombre: " + categoria.getCategoria() +
                    " | Descripción: " + categoria.getDescripcion());
        }
    }

    private void crearNuevaCategoria() {

        System.out.print("Nombre de la categoría: ");
        String nombre = sc.nextLine();

        System.out.print("Descripción: ");
        String descripcion = sc.nextLine();

        if (servicioCategoria.crearCategoria(nombre, descripcion)) {
            System.out.println("Categoría creada correctamente");
        } else {
            System.out.println("Error al crear categoría");
        }
    }

    private void eliminarCategoria() {
        verTodasLasCategorias();

        System.out.print("Ingrese ID de la categoría a eliminar: ");
        Integer categoriaId = sc.nextInt();

        if (servicioCategoria.eliminarCategoria(categoriaId)) {
            System.out.println("Categoría eliminada correctamente");
        } else {
            System.out.println("Error al eliminar categoría");
        }
    }

    private void verEstadisticas() {
        System.out.println("\n=== ESTADÍSTICAS DEL SISTEMA ===");

        // Total usuarios por rol
        List<Usuarios> todosUsuarios = servicioUsuario.obtenerTodosLosUsuarios();
        long admins = todosUsuarios.stream().filter(u -> u.getRole() == Rol.Admin).count();
        long profesores = todosUsuarios.stream().filter(u -> u.getRole() == Rol.Profesor).count();
        long alumnos = todosUsuarios.stream().filter(u -> u.getRole() == Rol.Alumno).count();

        System.out.println("Total Usuarios: " + todosUsuarios.size());
        System.out.println("- Admins: " + admins);
        System.out.println("- Profesores: " + profesores);
        System.out.println("- Alumnos: " + alumnos);

        // Total categorías
        List<Categorias> categorias = servicioCategoria.obtenerTodasLasCategorias();
        System.out.println("Total Categorías: " + categorias.size());

        // Total tests
        List<Test> tests = servicioTest.obtenerTodosLosTests();
        System.out.println("Total Tests: " + tests.size());
    }

    private void configuracionGlobal() {
        System.out.println("=== CONFIGURACIÓN ACTUAL ===");
        System.out.println("Número de preguntas por test: " + configuracion.getNumeroPreguntasTest());
        System.out.println("Respuestas múltiples: " + (configuracion.isRespuestasMultiples() ? "Sí" : "No"));
        System.out.println("Cuentan negativas: " + (configuracion.isCuentanNegativas() ? "Sí" : "No"));
        System.out.println("Mínimo para aprobar: " + configuracion.getMinimoAprobar());

        System.out.println("""
                
                ¿Qué desea modificar?
                1. Número de preguntas por test
                2. Respuestas múltiples
                3. Cuentan negativas
                4. Mínimo para aprobar
                5. Volver al menú principal""");

        System.out.print("Seleccione una opción: ");
        int opcion = sc.nextInt();

        switch (opcion) {
            case 1:
                System.out.print("Nuevo número de preguntas: ");
                int nuevasPreguntas = sc.nextInt();
                configuracion.setNumeroPreguntasTest(nuevasPreguntas);
                configuracion.guardarConfiguracion();
                System.out.println("Configuración actualizada");
                break;

            case 2:
                System.out.print("Respuestas múltiples (1=Sí, 2=No): ");
                int respMultiples = sc.nextInt();
                configuracion.setRespuestasMultiples(respMultiples == 1);
                configuracion.guardarConfiguracion();
                System.out.println("Configuración actualizada");
                break;

            case 3:
                System.out.print("Cuentan negativas (1=Sí, 2=No): ");
                int negativas = sc.nextInt();
                configuracion.setCuentanNegativas(negativas == 1);
                configuracion.guardarConfiguracion();
                System.out.println("Configuración actualizada");
                break;

            case 4:
                System.out.print("Nuevo mínimo para aprobar: ");
                int nuevoMinimo = sc.nextInt();
                configuracion.setMinimoAprobar(nuevoMinimo);
                configuracion.guardarConfiguracion();
                System.out.println("Configuración actualizada");
                break;

            case 5:
                return;

            default:
                System.out.println("Opción no válida");
        }
    }
}