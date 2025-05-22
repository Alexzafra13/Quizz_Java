package com.osuna.alejandro.quizzconsola.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AccesoFicheros {

    public static List<String> leerFichero(String rutaArchivo) throws IOException {

        List<String> lineas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {

            String linea;

            while ((linea = br.readLine()) != null) {

                lineas.add(linea);
            }
        }

        return lineas;
    }


    public static void escribirFichero(String rutaArchivo, List<String> lineas) throws IOException {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {

            for (String linea : lineas) {

                bw.write(linea);

                bw.newLine();
            }
        }
    }

}