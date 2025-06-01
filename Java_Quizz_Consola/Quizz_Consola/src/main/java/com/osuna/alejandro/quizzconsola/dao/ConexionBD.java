package com.osuna.alejandro.quizzconsola.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private final String ruta_bd = "jdbc:mysql://localhost:3306/quizz";
    private final String usuario = "root";
    private final String password = "Ale13091992!";
    private static Connection conexion;

    public ConexionBD() {
    }

    public Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {

                conexion = DriverManager.getConnection(ruta_bd, usuario, password);

                System.out.println("Conexión a la base de datos establecida correctamente");
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
                System.out.println("Conexión a la base de datos cerrada correctamente");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

}