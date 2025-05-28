package com.osuna.alejandro.quizzconsola.servicio;

import com.osuna.alejandro.quizzconsola.dao.implementaciones.TestDAOImpl;
import com.osuna.alejandro.quizzconsola.dao.implementaciones.TestPreguntasDAOImpl;
import com.osuna.alejandro.quizzconsola.dao.interfaces.TestDAO;
import com.osuna.alejandro.quizzconsola.dao.interfaces.TestPreguntasDAO;
import com.osuna.alejandro.quizzconsola.modelos.Test;
import com.osuna.alejandro.quizzconsola.modelos.Preguntas;
import com.osuna.alejandro.quizzconsola.util.ConfiguracionManager;

import java.time.LocalDate;
import java.util.List;

public class ServicioTest {

    private TestDAO testDAO;
    private TestPreguntasDAO testPreguntasDAO;
    private ConfiguracionManager configuracion;

    public ServicioTest() {
        this.testDAO = new TestDAOImpl();
        this.testPreguntasDAO = new TestPreguntasDAOImpl();
        this.configuracion = new ConfiguracionManager();
    }

    // CRUD
    public boolean crearTest(String titulo, String descripcion, Integer creadoPor) {
        Test nuevoTest = new Test(null, titulo, descripcion, creadoPor, LocalDate.now());

        boolean resultado = testDAO.insertar(nuevoTest);

        if (resultado) {
            System.out.println("Test creado correctamente");
        } else {
            System.out.println("Error al crear el test");
        }

        return resultado;
    }

    public Test buscarPorId(Integer id) {
        return testDAO.buscarPorId(id);
    }

    public List<Test> obtenerTodosLosTests() {
        return testDAO.obtenerTodos();
    }

    public boolean eliminarTest(Integer id) {
        boolean resultado = testDAO.eliminar(id);

        if (resultado) {
            System.out.println("Test eliminado correctamente");
        } else {
            System.out.println("Error al eliminar el test");
        }

        return resultado;
    }

    public boolean agregarPreguntaATest(Integer testId, Integer preguntaId) {
        boolean resultado = testPreguntasDAO.insertar(testId, preguntaId);

        if (resultado) {
            System.out.println("Pregunta agregada al test correctamente");
        } else {
            System.out.println("Error al agregar pregunta al test");
        }

        return resultado;
    }

    public List<Preguntas> obtenerPreguntasDeTest(Integer testId) {
        return testPreguntasDAO.obtenerPreguntasPorTest(testId);
    }

    public boolean validarTestCompleto(Integer testId) {
        List<Preguntas> lista_p = obtenerPreguntasDeTest(testId);
        int minPreguntas = configuracion.getNumeroPreguntasTest();

        if (lista_p.size() < minPreguntas) {
            System.out.println("El test debe tener al menos " + minPreguntas + " lista_p");
            return false;
        }

        return true;
    }
}