package com.osuna.alejandro.quizzconsola.dao.implementaciones;

import com.osuna.alejandro.quizzconsola.dao.ConexionBD;
import com.osuna.alejandro.quizzconsola.dao.interfaces.OpcionDAO;
import com.osuna.alejandro.quizzconsola.modelos.Opciones;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OpcionDAOImpl implements OpcionDAO {

    private ConexionBD conDB;

    public OpcionDAOImpl() {
        this.conDB = new ConexionBD();
    }

    // Metodo de mapeo
    private Opciones mapearOpcion(ResultSet rs) throws SQLException {
        return new Opciones(
                rs.getInt("id"),
                rs.getInt("pregunta_id"),
                rs.getString("opcion_text"),
                rs.getBoolean("is_correcta")
        );
    }

    private void validarOpcionExiste(Integer id) {
        if (buscarPorId(id) == null) {
            throw new RuntimeException("Opción con ID " + id + " no existe");
        }
    }

    @Override
    public Opciones buscarPorId(Integer id) {
        Opciones opcion = null;

        try (PreparedStatement pst = conDB.getConexion().prepareStatement("SELECT * FROM opciones WHERE id = ?")) {

            pst.setInt(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    opcion = mapearOpcion(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar opción por ID: " + id + " : " + e);
        }

        return opcion;
    }

    @Override
    public boolean insertar(Opciones opcion) {
        try (PreparedStatement pst = conDB.getConexion().prepareStatement(
                "INSERT INTO opciones (pregunta_id, opcion_text, is_correcta) VALUES (?, ?, ?)")) {

            pst.setInt(1, opcion.getPreguntas_id());
            pst.setString(2, opcion.getOpcion_text());
            pst.setBoolean(3, opcion.isIs_correcta());

            int filasAfectadas = pst.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar opción: " + e);
        }
    }

    @Override
    public boolean actualizar(Opciones opcion) {
        // Verificar que existe
        validarOpcionExiste(opcion.getId());

        try (PreparedStatement pst = conDB.getConexion().prepareStatement(
                "UPDATE opciones SET pregunta_id = ?, opcion_text = ?, is_correcta = ? WHERE id = ?")) {

            pst.setInt(1, opcion.getPreguntas_id());
            pst.setString(2, opcion.getOpcion_text());
            pst.setBoolean(3, opcion.isIs_correcta());
            pst.setInt(4, opcion.getId());

            int filasAfectadas = pst.executeUpdate();
            return filasAfectadas == 1;

        } catch (SQLException e) {
            throw new RuntimeException("No se pudo actualizar la opción con ID: " + opcion.getId() + " : " + e);
        }
    }

    @Override
    public boolean eliminar(Integer id) {
        // Verificar que existe
        validarOpcionExiste(id);

        try (PreparedStatement pst = conDB.getConexion().prepareStatement("DELETE FROM opciones WHERE id = ?")) {

            pst.setInt(1, id);

            int filasAfectadas = pst.executeUpdate();
            return filasAfectadas == 1;

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar opción con ID: " + id + " : " + e);
        }
    }

    @Override
    public List<Opciones> buscarPorPregunta(Integer preguntaId) {
        List<Opciones> opciones = new ArrayList<>();

        try (PreparedStatement pst = conDB.getConexion().prepareStatement
                ("SELECT * FROM opciones WHERE pregunta_id = ?")) {

            pst.setInt(1, preguntaId);

            try (ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {
                    opciones.add(mapearOpcion(rs));
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar opciones por pregunta: " + preguntaId + " : " + e);
        }

        return opciones;
    }

    @Override
    public Opciones buscarRespuestaCorrecta(Integer preguntaId) {
        Opciones opcion = null;

        try (PreparedStatement pst = conDB.getConexion().prepareStatement
                ("SELECT * FROM opciones WHERE pregunta_id = ? AND is_correcta = true")) {

            pst.setInt(1, preguntaId);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    opcion = mapearOpcion(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar respuesta correcta para pregunta: " + preguntaId + " : " + e);
        }

        return opcion;
    }

    @Override
    public List<Opciones> obtenerTodas() {
        List<Opciones> opciones = new ArrayList<>();

        try (PreparedStatement pst = conDB.getConexion().prepareStatement("SELECT * FROM opciones")) {

            try (ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {
                    opciones.add(mapearOpcion(rs));
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todas las opciones: " + e);
        }

        return opciones;
    }
}