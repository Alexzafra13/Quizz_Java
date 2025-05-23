package com.osuna.alejandro.quizzconsola.dao.implementaciones;

import com.osuna.alejandro.quizzconsola.dao.ConexionBD;
import com.osuna.alejandro.quizzconsola.dao.interfaces.PreguntasDAO;
import com.osuna.alejandro.quizzconsola.modelos.Preguntas;
import com.osuna.alejandro.quizzconsola.modelos.enums.Dificultad;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PreguntasDAOImpl implements PreguntasDAO {

    private ConexionBD conDB;

    public PreguntasDAOImpl() {
        this.conDB = new ConexionBD();
    }

    // Metodo de mapeo
    private Preguntas mapearPregunta(ResultSet rs) throws SQLException {
        return new Preguntas(
                rs.getInt("id"),
                rs.getString("preguntas_text"),
                rs.getInt("categoria_id"),
                Dificultad.valueOf(rs.getString("dificultad")),
                rs.getDate("created_at").toLocalDate()
        );
    }

    private void validarPreguntaExiste(Integer id) {
        if (buscarPorId(id) == null) {
            throw new RuntimeException("Pregunta con ID " + id + " no existe");
        }
    }

    @Override
    public Preguntas buscarPorId(Integer id) {
        Preguntas pregunta = null;

        try (PreparedStatement pst = conDB.getConexion().prepareStatement("SELECT * FROM preguntas WHERE id = ?")) {

            pst.setInt(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    pregunta = mapearPregunta(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar pregunta por ID: " + id + " : " + e);
        }

        return pregunta;
    }

    @Override
    public boolean insertar(Preguntas pregunta) {
        try (PreparedStatement pst = conDB.getConexion().prepareStatement(
                "INSERT INTO preguntas (preguntas_text, categoria_id, dificultad, created_at) VALUES (?, ?, ?, ?)")) {

            pst.setString(1, pregunta.getPreguntas_text());
            pst.setInt(2, pregunta.getCategoria_id());
            pst.setString(3, pregunta.getDificultad().toString());
            pst.setDate(4, java.sql.Date.valueOf(pregunta.getCreate_at()));

            int filasAfectadas = pst.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar pregunta: " + e);
        }
    }

    @Override
    public boolean actualizar(Preguntas pregunta) {
        // Verificar que existe
        validarPreguntaExiste(pregunta.getId());

        try (PreparedStatement pst = conDB.getConexion().prepareStatement(
                "UPDATE preguntas SET preguntas_text = ?, categoria_id = ?, dificultad = ?, created_at = ? WHERE id = ?")) {

            pst.setString(1, pregunta.getPreguntas_text());
            pst.setInt(2, pregunta.getCategoria_id());
            pst.setString(3, pregunta.getDificultad().toString());
            pst.setDate(4, java.sql.Date.valueOf(pregunta.getCreate_at()));
            pst.setInt(5, pregunta.getId());

            int filasAfectadas = pst.executeUpdate();
            return filasAfectadas == 1;

        } catch (SQLException e) {
            throw new RuntimeException("No se pudo actualizar la pregunta con ID: " + pregunta.getId() + " : " + e);
        }
    }

    @Override
    public boolean eliminar(Integer id) {
        // Verificar que existe
        validarPreguntaExiste(id);

        try (PreparedStatement pst = conDB.getConexion().prepareStatement("DELETE FROM preguntas WHERE id = ?")) {

            pst.setInt(1, id);

            int filasAfectadas = pst.executeUpdate();
            return filasAfectadas == 1;

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar pregunta con ID: " + id + " : " + e);
        }
    }

    @Override
    public List<Preguntas> obtenerTodas() {
        List<Preguntas> preguntas = new ArrayList<>();

        try (PreparedStatement pst = conDB.getConexion().prepareStatement("SELECT * FROM preguntas")) {

            try (ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {
                    preguntas.add(mapearPregunta(rs));
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todas las preguntas: " + e);
        }

        return preguntas;
    }

    @Override
    public List<Preguntas> buscarPorCategoria(Integer categoriaId) {
        List<Preguntas> preguntas = new ArrayList<>();

        try (PreparedStatement pst = conDB.getConexion().prepareStatement("SELECT * FROM preguntas WHERE categoria_id = ?")) {

            pst.setInt(1, categoriaId);

            try (ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {
                    preguntas.add(mapearPregunta(rs));
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar preguntas por categoria: " + categoriaId + " : " + e);
        }

        return preguntas;
    }

    @Override
    public List<Preguntas> buscarPorDificultad(Dificultad dificultad) {
        List<Preguntas> preguntas = new ArrayList<>();

        try (PreparedStatement pst = conDB.getConexion().prepareStatement("SELECT * FROM preguntas WHERE dificultad = ?")) {

            pst.setString(1, dificultad.toString());

            try (ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {
                    preguntas.add(mapearPregunta(rs));
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar preguntas por dificultad: " + dificultad + " : " + e);
        }

        return preguntas;
    }

    @Override
    public List<Preguntas> obtenerAleatorias(int cantidad) {
        List<Preguntas> preguntas = new ArrayList<>();

        try (PreparedStatement pst = conDB.getConexion().prepareStatement("SELECT * FROM preguntas ORDER BY RAND() LIMIT ?")) {

            pst.setInt(1, cantidad);

            try (ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {
                    preguntas.add(mapearPregunta(rs));
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener preguntas aleatorias: " + e);
        }

        return preguntas;
    }
}