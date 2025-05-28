package com.osuna.alejandro.quizzconsola.controlador_consola;

import com.osuna.alejandro.quizzconsola.dao.implementaciones.PreguntasDAOImpl;
import com.osuna.alejandro.quizzconsola.dao.interfaces.PreguntasDAO;
import com.osuna.alejandro.quizzconsola.modelos.*;
import com.osuna.alejandro.quizzconsola.modelos.enums.Dificultad;
import com.osuna.alejandro.quizzconsola.servicio.*;
import com.osuna.alejandro.quizzconsola.util.ConfiguracionManager;

import java.util.List;
import java.util.Scanner;

public class MenuProfesor {

    private Scanner sc = new Scanner(System.in);
    private ServicioPregunta servicioPregunta;
    private ServicioCategoria servicioCategoria;
    private ServicioTest servicioTest;
    private ServicioUsuario servicioUsuario;
    private ConfiguracionManager configuracion;
    private ServicioResultado servicioResultado;

    public MenuProfesor() {
        PreguntasDAO preguntasDAO = new PreguntasDAOImpl();
        this.servicioPregunta = new ServicioPregunta(preguntasDAO);
        this.servicioCategoria = new ServicioCategoria();
        this.servicioTest = new ServicioTest();
        this.servicioUsuario = new ServicioUsuario();
        this.configuracion = new ConfiguracionManager();
        this.servicioResultado = new ServicioResultado();
    }

    public void mostrarMenuProfesor() {

        int opcion;
        boolean salir = false;

        do {
            System.out.println("""
                  1.Insertar Preguntas
                  2.Crear Un Test
                  3.Ver Resultados
                  4.Configuracion
                  5.Salir
                  Seleccione una opción:""");

            opcion = sc.nextInt();

            switch (opcion) {
                case 1:
                    insertarPregunta();
                    break;
                case 2:
                    crearTest();
                    break;
                case 3:
                    verResultados();
                    break;
                case 4:
                    configuracion();
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

    private void insertarPregunta() {

        int opcionCat;
        String textoPregunta;

        System.out.println("<--- Categorías Disponibles --->");
        List<Categorias> categorias = servicioCategoria.obtenerTodasLasCategorias();

        // Mostrar categorías con números
        for (int i = 0; i < categorias.size(); i++) {
            System.out.println((i + 1) + ". " + categorias.get(i).getCategoria());
        }

        System.out.print("Seleccione categoría: ");
        opcionCat = sc.nextInt() - 1;
        Integer categoriaId = categorias.get(opcionCat).getId();

        System.out.print("Texto de la pregunta: ");
        textoPregunta = sc.nextLine();

        int dif;
        do {
            System.out.println("""
               1.Facil
               2.Intermedio
               3.Difícil""");

            dif = sc.nextInt();

            if (dif < 1 || dif > 3) {
                System.out.println("Opción no válida, Intente de nuevo del 1 al 3");
            }

        } while (dif < 1 || dif > 3);

        Dificultad dificultad = (dif == 1) ? Dificultad.Facil :
                (dif == 2) ? Dificultad.Intermedio : Dificultad.Dificil;

        this.servicioPregunta.crearPregunta(textoPregunta, categoriaId, dificultad);
    }

    private void crearTest() {

        System.out.print("Título del test: ");

        String titulo = sc.nextLine();

        System.out.print("Descripción: ");

        String descripcion = sc.nextLine();

        Integer creadoPor = ServicioAutentificacion.getUsuarioActual().getId();

        if (servicioTest.crearTest(titulo, descripcion, creadoPor)) {
            System.out.println("Test creado exitosamente!");

            List<Test> todosLosTests = servicioTest.obtenerTodosLosTests();

            Test testCreado = todosLosTests.get(todosLosTests.size() - 1);

            Integer testId = testCreado.getId();

            System.out.println("Agrega preguntas al test");

            boolean seguirAgregando = true;

            while (seguirAgregando) {

                System.out.println("Preguntas disponibles:");

                List<Preguntas> preguntasDisponibles = servicioPregunta.obtenerTodasLasPreguntas();

                for (int i = 0; i < preguntasDisponibles.size(); i++) {
                    System.out.println((i + 1) + ". " + preguntasDisponibles.get(i).getPreguntas_text());
                }

                System.out.print("Seleccione pregunta (1-" + preguntasDisponibles.size() + "): ");

                int opcionPregunta = sc.nextInt() - 1;

                if (opcionPregunta >= 0 && opcionPregunta < preguntasDisponibles.size()) {

                    Integer preguntaId = preguntasDisponibles.get(opcionPregunta).getId();
                    servicioTest.agregarPreguntaATest(testId, preguntaId);
                }

                System.out.print("¿Agregar otra pregunta? (1=Sí, 2=No): ");
                int continuar = sc.nextInt();
                seguirAgregando = (continuar == 1);
            }

            if (servicioTest.validarTestCompleto(testId)) {
                System.out.println("Test creado y configurado correctamente!");
            } else {
                System.out.println("Advertencia: El test no cumple con los requisitos mínimos");
            }

        } else {
            System.out.println("Error al crear el test");
        }
    }

    private void verResultados() {

        System.out.println("=== VER RESULTADOS ===");
        System.out.println("""
           1. Ver resultados por test
           2. Ver resultados por usuario
           3. Volver al menú principal""");

        System.out.print("Seleccione una opción: ");
        int opcion = sc.nextInt();

        switch (opcion) {
            case 1:
                verResultadosPorTest();
                break;
            case 2:
                verResultadosPorUsuario();
                break;
            case 3:
                return;
            default:
                System.out.println("Opción no válida");
        }
    }

    private void verResultadosPorTest() {

        System.out.println("Tests disponibles:");

        List<Test> tests = servicioTest.obtenerTodosLosTests();

        for (int i = 0; i < tests.size(); i++) {
            System.out.println((i + 1) + ". " + tests.get(i).getTitulo());
        }

        System.out.print("Seleccione test: ");

        int opcionTest = sc.nextInt() - 1;

        if (opcionTest >= 0 && opcionTest < tests.size()) {

            Integer testId = tests.get(opcionTest).getId();

            ServicioResultado servicioResultado = new ServicioResultado();

            List<Test_Resultados> resultados = servicioResultado.obtenerResultadosPorTest(testId);

            if (resultados.isEmpty()) {

                System.out.println("No hay resultados para este test");

            } else {
                System.out.println("\n=== RESULTADOS DEL TEST: " + tests.get(opcionTest).getTitulo() + " ===");

                for (Test_Resultados resultado : resultados) {

                    System.out.println("Usuario: " + resultado.getUsuario_id() +
                            " Puntuación: " + resultado.getPuntuacion() +
                            " Fecha: " + resultado.getFin());
                }
            }
        }
    }

    private void verResultadosPorUsuario() {

        System.out.print("Ingrese email del usuario: ");

        String email = sc.nextLine();

        Usuarios usuario = servicioUsuario.buscarPorEmail(email);

        if (usuario == null) {

            System.out.println("Usuario no encontrado");

            return;
        }

        List<Test_Resultados> resultados = servicioResultado.obtenerResultadosPorUsuario(usuario.getId());

        if (resultados.isEmpty()) {
            System.out.println("No hay resultados para este usuario");
        } else {
            System.out.println("\n<--- Resultados de: " + usuario.getUsername() + " --->");
            for (Test_Resultados resultado : resultados) {
                System.out.println("Test: " + resultado.getTest_id() +
                        " Puntuación: " + resultado.getPuntuacion() +
                        " Fecha: " + resultado.getFin());
            }
        }
    }

    private void configuracion() {

        System.out.println("<--- Configuracion Actual --->");
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