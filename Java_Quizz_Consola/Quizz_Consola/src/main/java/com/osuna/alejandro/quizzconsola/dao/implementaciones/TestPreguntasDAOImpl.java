
package com.osuna.alejandro.quizzconsola.dao.implementaciones;

import com.osuna.alejandro.quizzconsola.dao.ConexionBD;
import com.osuna.alejandro.quizzconsola.dao.interfaces.TestPreguntasDAO;
import com.osuna.alejandro.quizzconsola.modelos.Preguntas;
import com.osuna.alejandro.quizzconsola.modelos.enums.Dificultad;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TestPreguntasDAOImpl implements TestPreguntasDAO {

    private ConexionBD conDB;

    public TestPreguntasDAOImpl() {
        this.conDB = new ConexionBD();
    }

    // Metodo de mapeo para preguntas
    private Preguntas mapearPregunta(ResultSet rs) throws SQLException {
        return new Preguntas(
                rs.getInt("id"),
                rs.getString("preguntas_text"),
                rs.getInt("categoria_id"),
                Dificultad.valueOf(rs.getString("dificultad")),
                rs.getDate("created_at").toLocalDate()
        );
    }

    private boolean existeRelacion(Integer testId, Integer preguntaId) {
        try (PreparedStatement pst = conDB.getConexion().prepareStatement(
                "SELECT COUNT(*) FROM test_preguntas WHERE test_id = ? AND pregunta_id = ?")) {

            pst.setInt(1, testId);
            pst.setInt(2, preguntaId);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar relación test-pregunta: " + e);
        }

        return false;
    }

    @Override
    public boolean insertar(Integer testId, Integer preguntaId) {
        // Verificar que no existe ya la relación
        if (existeRelacion(testId, preguntaId)) {
            throw new RuntimeException("La relación entre test " + testId +
                    " y pregunta " + preguntaId + " ya existe");
        }

        try (PreparedStatement pst = conDB.getConexion().prepareStatement(
                "INSERT INTO test_preguntas (test_id, pregunta_id) VALUES (?, ?)")) {

            pst.setInt(1, testId);
            pst.setInt(2, preguntaId);

            int filasAfectadas = pst.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar relación test-pregunta: " + e);
        }
    }

    @Override
    public boolean eliminar(Integer testId, Integer preguntaId) {
        // Verificar que existe la relación
        if (!existeRelacion(testId, preguntaId)) {
            throw new RuntimeException("La relación entre test " + testId +
                    " y pregunta " + preguntaId + " no existe");
        }

        try (PreparedStatement pst = conDB.getConexion().prepareStatement(
                "DELETE FROM test_preguntas WHERE test_id = ? AND pregunta_id = ?")) {

            pst.setInt(1, testId);
            pst.setInt(2, preguntaId);

            int filasAfectadas = pst.executeUpdate();
            return filasAfectadas == 1;

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar relación test-pregunta: " + e);
        }
    }

    @Override
    public List<Preguntas> obtenerPreguntasPorTest(Integer testId) {
        List<Preguntas> preguntas = new ArrayList<>();

        try (PreparedStatement pst = conDB.getConexion().prepareStatement(
                "SELECT preguntas.* FROM preguntas " +
                        "JOIN test_preguntas ON preguntas.id = test_preguntas.pregunta_id " +
                        "WHERE test_preguntas.test_id = ?")) {

            pst.setInt(1, testId);

            try (ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {
                    preguntas.add(mapearPregunta(rs));
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener preguntas por test: " + testId + " : " + e);
        }

        return preguntas;
    }

    @Override
    public List<Integer> obtenerTestsPorPregunta(Integer preguntaId) {
        List<Integer> testIds = new ArrayList<>();

        try (PreparedStatement pst = conDB.getConexion().prepareStatement(
                "SELECT test_id FROM test_preguntas WHERE pregunta_id = ?")) {

            pst.setInt(1, preguntaId);

            try (ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {
                    testIds.add(rs.getInt("test_id"));
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener tests por pregunta: " + preguntaId + " : " + e);
        }

        return testIds;
    }
}