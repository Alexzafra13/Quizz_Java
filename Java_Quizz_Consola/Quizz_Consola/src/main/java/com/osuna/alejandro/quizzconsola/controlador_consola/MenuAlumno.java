package com.osuna.alejandro.quizzconsola.controlador_consola;

import com.osuna.alejandro.quizzconsola.dao.implementaciones.PreguntasDAOImpl;
import com.osuna.alejandro.quizzconsola.dao.interfaces.PreguntasDAO;
import com.osuna.alejandro.quizzconsola.modelos.*;
import com.osuna.alejandro.quizzconsola.servicio.*;

import java.util.List;
import java.util.Scanner;

public class MenuAlumno {

    private Scanner sc = new Scanner(System.in);
    private ServicioPregunta servicioPregunta;
    private ServicioTest servicioTest;
    private ServicioResultado servicioResultado;

    public MenuAlumno() {
        PreguntasDAO preguntasDAO = new PreguntasDAOImpl();
        this.servicioPregunta = new ServicioPregunta(preguntasDAO);
        this.servicioTest = new ServicioTest();
        this.servicioResultado = new ServicioResultado();
    }

    public void mostrarMenuAlumno() {

        int opcion;
        boolean salir = false;

        do {
            System.out.println("""
                  1. Hacer un test
                  2. Ver mis resultados
                  3. Repetir test
                  4. Salir
                  Seleccione una opción:""");

            opcion = sc.nextInt();

            switch (opcion) {
                case 1:
                    hacerTest();
                    break;
                case 2:
                    verMisResultados();
                    break;
                case 3:
                    repetirTest();
                    break;
                case 4:
                    salir = true;
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida");
            }

        } while (!salir);
    }

    private void hacerTest() {

        System.out.println("<--- Test Disponibles --->");

        List<Test> tests = servicioTest.obtenerTodosLosTests();

        for (int i = 0; i < tests.size(); i++) {

            System.out.println((i + 1) + ". " + tests.get(i).getTitulo());
        }

        System.out.print("Seleccione test: ");

        int opcionTest = sc.nextInt() - 1;

        if (opcionTest >= 0 && opcionTest < tests.size()) {

            Integer testId = tests.get(opcionTest).getId();

            realizarTest(testId);
        }
    }

    private void realizarTest(Integer testId) {

        Integer usuarioId = 1;

        List<Preguntas> preguntas = servicioTest.obtenerPreguntasDeTest(testId);

        if (preguntas.isEmpty()) {
            System.out.println("El test no tiene preguntas");
            return;
        }

        int respuestasCorrectas = 0;

        System.out.println("\n=== INICIANDO TEST ===");

        for (int i = 0; i < preguntas.size(); i++) {
            Preguntas pregunta = preguntas.get(i);

            System.out.println("\nPregunta " + (i + 1) + ": " + pregunta.getPreguntas_text());

            List<Opciones> opciones = servicioPregunta.obtenerOpcionesDePregunta(pregunta.getId());


            for (int j = 0; j < opciones.size(); j++) {

                System.out.println((j + 1) + ". " + opciones.get(j).getOpcion_text());
            }

            System.out.print("Tu respuesta (1-" + opciones.size() + "): ");
            int respuestaUsuario = sc.nextInt() - 1;

            if (respuestaUsuario >= 0 && respuestaUsuario < opciones.size()) {
                Opciones opcionSeleccionada = opciones.get(respuestaUsuario);


                servicioResultado.guardarRespuesta(usuarioId, testId, pregunta.getId(), opcionSeleccionada.getId());


                if (opcionSeleccionada.isIs_correcta()) {

                    respuestasCorrectas++;

                    System.out.println("¡Correcto!");

                } else {

                    System.out.println("Incorrecto.");
                }
            }
        }


        servicioResultado.guardarResultadoTest(usuarioId, testId, respuestasCorrectas, preguntas.size());


        double puntuacion = servicioResultado.calcularPuntuacion(respuestasCorrectas, preguntas.size());

        System.out.println("\n<--- RESULTADO --->");
        System.out.println("Respuestas correctas: " + respuestasCorrectas + "/" + preguntas.size());
        System.out.println("Puntuación: " + puntuacion);

        if (servicioResultado.validadorAprueba(puntuacion)) {
            System.out.println("¡APROBADO!");
        } else {
            System.out.println("No aprobado :(");
        }
    }

    private void verMisResultados() {

        Integer usuarioId = ServicioAutentificacion.getUsuarioActual().getId();

        List<Test_Resultados> resultados = servicioResultado.obtenerResultadosPorUsuario(usuarioId);

        if (resultados.isEmpty()) {
            System.out.println("No tienes resultados aún");
        } else {
            System.out.println("\n<--- MIS RESULTADOS --->");
            for (Test_Resultados resultado : resultados) {
                System.out.println("Test: " + resultado.getTest_id() +
                        " Puntuación: " + resultado.getPuntuacion() +
                        " Fecha: " + resultado.getFin());
            }
        }
    }

    private void repetirTest() {

        hacerTest();
    }
}