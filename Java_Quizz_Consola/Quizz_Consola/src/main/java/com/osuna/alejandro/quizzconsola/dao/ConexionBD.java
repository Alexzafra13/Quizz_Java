package com.osuna.alejandro.quizzconsola.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionBD {

    private final String ruta_bd = "jdbc:mysql://localhost:3306/quizz";
    private final String usuario = "root";
    private final String password = "Ale13091992!";
    private static Connection conexion;

    public ConexionBD() {
        // Creo la base de datos al inicializar (si no existe claro)
        crearBaseDatos();
    }

    private void crearBaseDatos() {
        try {
            // Conecta sin especificar base de datos, esto lo busque en internet por que segun hice,
            // el programa no funcionaba ya que se intentaba por primera vez conectar a una base de datos que no existe,
            // asi que se crea primero, y luego se ejecuta la clase que crea las tablas vistas y demas
            Connection connTemp = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/",
                    usuario,
                    password
            );
            Statement stmt = connTemp.createStatement();
            stmt.execute("CREATE DATABASE IF NOT EXISTS quizz");
            stmt.close();
            connTemp.close();
            System.out.println("Base de datos verificada/creada.");
        } catch (SQLException e) {
            System.err.println("Error al crear base de datos: " + e.getMessage());
        }
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