package com.osuna.alejandro.quizzconsola.util;

import java.io.IOException;
import java.util.List;

public class ConfiguracionManager {

    private static final String RUTA_CONFIGURACION = "src/main/resources/configuracion.txt";

    private int numeroPreguntasTest;
    private boolean respuestasMultiples;
    private boolean cuentanNegativas;
    private int minimoAprobar;

    public ConfiguracionManager() {
        cargarConfiguracion();
    }

    public void cargarConfiguracion() {
        try {
            List<String> lineas = AccesoFicheros.leerFichero(RUTA_CONFIGURACION);

            for (String linea : lineas) {
                if (linea.contains("numeroPreguntasTest")) {
                    numeroPreguntasTest = extraerValorNumerico(linea);
                } else if (linea.contains("respuestasMultiples")) {
                    respuestasMultiples = extraerValorBooleano(linea);
                } else if (linea.contains("cuentanNegativas")) {
                    cuentanNegativas = extraerValorBooleano(linea);
                } else if (linea.contains("minimoAprobar")) {
                    minimoAprobar = extraerValorNumerico(linea);
                }
            }

            System.out.println("Configuración cargada correctamente");

        } catch (IOException e) {

            System.err.println("Error al cargar la configuración: " + e.getMessage());

            numeroPreguntasTest = 10;
            respuestasMultiples = false;
            cuentanNegativas = false;
            minimoAprobar = 5;
        }
    }

    public void guardarConfiguracion() {
        try {
            List<String> lineas = List.of(
                    "\"numeroPreguntasTest\": " + numeroPreguntasTest,
                    "\"respuestasMultiples\": " + (respuestasMultiples ? "Si" : "No"),
                    "\"cuentanNegativas\": " + (cuentanNegativas ? "Si" : "No"),
                    "\"minimoAprobar\": " + minimoAprobar
            );

            AccesoFicheros.escribirFichero(RUTA_CONFIGURACION, lineas);

            System.out.println("Configuración guardada correctamente");

        } catch (IOException e) {

            System.err.println("Error al guardar la configuración: " + e.getMessage());
        }
    }

    private int extraerValorNumerico(String linea) {

        String[] partes = linea.split(":");
        if (partes.length > 1) {
            String valorStr = partes[1].trim();
            return Integer.parseInt(valorStr);
        }
        return 0;
    }

    private boolean extraerValorBooleano(String linea) {

        String[] partes = linea.split(":");
        if (partes.length > 1) {
            String valorStr = partes[1].trim();
            return valorStr.equalsIgnoreCase("Si");
        }
        return false;
    }

    public int getNumeroPreguntasTest() {
        return numeroPreguntasTest;
    }

    public boolean isRespuestasMultiples() {
        return respuestasMultiples;
    }

    public boolean isCuentanNegativas() {
        return cuentanNegativas;
    }

    public int getMinimoAprobar() {
        return minimoAprobar;
    }

    public void setNumeroPreguntasTest(int numeroPreguntasTest) {
        this.numeroPreguntasTest = numeroPreguntasTest;
    }

    public void setRespuestasMultiples(boolean respuestasMultiples) {
        this.respuestasMultiples = respuestasMultiples;
    }

    public void setCuentanNegativas(boolean cuentanNegativas) {
        this.cuentanNegativas = cuentanNegativas;
    }

    public void setMinimoAprobar(int minimoAprobar) {
        this.minimoAprobar = minimoAprobar;
    }

    @Override
    public String toString() {
        return "ConfiguracionManager{" +
                "numeroPreguntasTest=" + numeroPreguntasTest +
                ", respuestasMultiples=" + respuestasMultiples +
                ", cuentanNegativas=" + cuentanNegativas +
                ", minimoAprobar=" + minimoAprobar +
                '}';
    }
}