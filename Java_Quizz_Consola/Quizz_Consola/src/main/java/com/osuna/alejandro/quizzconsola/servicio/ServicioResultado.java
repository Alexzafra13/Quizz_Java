package com.osuna.alejandro.quizzconsola.servicio;

import com.osuna.alejandro.quizzconsola.dao.implementaciones.TestResultadoDAOImpl;
import com.osuna.alejandro.quizzconsola.dao.implementaciones.UsuarioRespuestaDAOImpl;
import com.osuna.alejandro.quizzconsola.dao.interfaces.TestResultadoDAO;
import com.osuna.alejandro.quizzconsola.dao.interfaces.UsuarioRespuestaDAO;
import com.osuna.alejandro.quizzconsola.modelos.*;
import com.osuna.alejandro.quizzconsola.util.ConfiguracionManager;

import java.time.LocalDate;
import java.util.List;

public class ServicioResultado {

    private TestResultadoDAO testResultadoDAO;
    private UsuarioRespuestaDAO usuarioRespuestaDAO;
    private ConfiguracionManager configuracion;

    public ServicioResultado() {
        this.testResultadoDAO = new TestResultadoDAOImpl();
        this.usuarioRespuestaDAO = new UsuarioRespuestaDAOImpl();
        this.configuracion = new ConfiguracionManager();
    }


    public boolean guardarRespuesta(Integer usuarioId, Integer testId, Integer preguntaId, Integer opcionId) {
        Usuario_Respuesta respuesta = new Usuario_Respuesta(
                null, usuarioId, testId, preguntaId, opcionId, LocalDate.now()
        );
        return usuarioRespuestaDAO.insertar(respuesta);
    }

    public boolean guardarResultadoTest(Integer usuarioId, Integer testId, int respuestasCorrectas, int totalPreguntas) {
        double puntuacion = calcularPuntuacion(respuestasCorrectas, totalPreguntas);

        Test_Resultados resultado = new Test_Resultados(
                null, usuarioId, testId, puntuacion, LocalDate.now(), LocalDate.now()
        );

        return testResultadoDAO.insertar(resultado);
    }

    public double calcularPuntuacion(int respuestasCorrectas, int totalPreguntas) {
        if (respuestasCorrectas < 0) respuestasCorrectas = 0;
        return (double) respuestasCorrectas / totalPreguntas * 10;
    }

    public boolean validadorAprueba(double puntuacion) {
        return puntuacion >= configuracion.getMinimoAprobar();
    }

    public List<Test_Resultados> obtenerResultadosPorUsuario(Integer usuarioId) {
        return testResultadoDAO.buscarPorUsuario(usuarioId);
    }

    public List<Test_Resultados> obtenerResultadosPorTest(Integer testId) {
        return testResultadoDAO.buscarPorTest(testId);
    }

    public Test_Resultados obtenerResultado(Integer usuarioId, Integer testId) {
        return testResultadoDAO.buscarPorUsuarioYTest(usuarioId, testId);
    }
}