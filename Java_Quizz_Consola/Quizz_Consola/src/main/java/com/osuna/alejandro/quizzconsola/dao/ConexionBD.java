package com.osuna.alejandro.quizzconsola.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private final String ruta_bd = "jdbc:sqlite:src/main/resources/database/DB_QUIZZ.db";
    private static Connection conexion;

    // Constructor simple
    public ConexionBD() {
    }

    public Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {

                conexion = DriverManager.getConnection(ruta_bd);

                System.out.println("Conexión a base de datos establecida correctamente");
            }
            return conexion;
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
            return null;
        }
    }

    public void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
                conexion = null;
                System.out.println("Conexión a base de datos cerrada correctamente");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

}