
package com.osuna.alejandro.quizzconsola.dao.implementaciones;

import com.osuna.alejandro.quizzconsola.dao.ConexionBD;
import com.osuna.alejandro.quizzconsola.dao.interfaces.TestResultadoDAO;
import com.osuna.alejandro.quizzconsola.modelos.Test_Resultados;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TestResultadoDAOImpl implements TestResultadoDAO {

    private ConexionBD conDB;

    public TestResultadoDAOImpl() {
        this.conDB = new ConexionBD();
    }

    // Metodo de mapeo
    private Test_Resultados mapearTestResultado(ResultSet rs) throws SQLException {
        return new Test_Resultados(
                rs.getInt("id"),
                rs.getInt("usuario_id"),
                rs.getInt("test_id"),
                rs.getDouble("puntuacion"),
                rs.getDate("inicio").toLocalDate(),
                rs.getDate("fin") != null ? rs.getDate("fin").toLocalDate() : null
        );
    }

    private void validarTestResultadoExiste(Integer id) {
        if (buscarPorId(id) == null) {
            throw new RuntimeException("Resultado de test con ID " + id + " no existe");
        }
    }

    @Override
    public Test_Resultados buscarPorId(Integer id) {
        Test_Resultados resultado = null;

        try (PreparedStatement pst = conDB.getConexion().prepareStatement
                ("SELECT * FROM test_resultados WHERE id = ?")) {

            pst.setInt(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    resultado = mapearTestResultado(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar resultado por ID: " + id + " : " + e);
        }

        return resultado;
    }

    @Override
    public boolean insertar(Test_Resultados resultado) {
        try (PreparedStatement pst = conDB.getConexion().prepareStatement(
                "INSERT INTO test_resultados (usuario_id, test_id, puntuacion, inicio, fin) VALUES (?, ?, ?, ?, ?)")) {

            pst.setInt(1, resultado.getUsuario_id());
            pst.setInt(2, resultado.getTest_id());
            pst.setDouble(3, resultado.getPuntuacion());
            pst.setDate(4, java.sql.Date.valueOf(resultado.getInicio()));
            pst.setDate(5, resultado.getFin() != null ? java.sql.Date.valueOf(resultado.getFin()) : null);

            int filasAfectadas = pst.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar resultado de test: " + e);
        }
    }

    @Override
    public boolean actualizar(Test_Resultados resultado) {
        // Verificar que existe
        validarTestResultadoExiste(resultado.getId());

        try (PreparedStatement pst = conDB.getConexion().prepareStatement(
                "UPDATE test_resultados SET usuario_id = ?, test_id = ?, puntuacion = ?, inicio = ?, fin = ? WHERE id = ?")) {

            pst.setInt(1, resultado.getUsuario_id());
            pst.setInt(2, resultado.getTest_id());
            pst.setDouble(3, resultado.getPuntuacion());
            pst.setDate(4, java.sql.Date.valueOf(resultado.getInicio()));
            pst.setDate(5, resultado.getFin() != null ? java.sql.Date.valueOf(resultado.getFin()) : null);
            pst.setInt(6, resultado.getId());

            int filasAfectadas = pst.executeUpdate();
            return filasAfectadas == 1;

        } catch (SQLException e) {
            throw new RuntimeException("No se pudo actualizar el resultado con ID: " + resultado.getId() + " : " + e);
        }
    }

    @Override
    public boolean eliminar(Integer id) {
        // Verificar que existe
        validarTestResultadoExiste(id);

        try (PreparedStatement pst = conDB.getConexion().prepareStatement("DELETE FROM test_resultados WHERE id = ?")) {

            pst.setInt(1, id);

            int filasAfectadas = pst.executeUpdate();
            return filasAfectadas == 1;

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar resultado con ID: " + id + " : " + e);
        }
    }

    @Override
    public List<Test_Resultados> buscarPorUsuario(Integer usuarioId) {
        List<Test_Resultados> resultados = new ArrayList<>();

        try (PreparedStatement pst = conDB.getConexion().prepareStatement("SELECT * FROM test_resultados WHERE usuario_id = ?")) {

            pst.setInt(1, usuarioId);

            try (ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {
                    resultados.add(mapearTestResultado(rs));
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar resultados por usuario: " + usuarioId + " : " + e);
        }

        return resultados;
    }

    @Override
    public List<Test_Resultados> buscarPorTest(Integer testId) {
        List<Test_Resultados> resultados = new ArrayList<>();

        try (PreparedStatement pst = conDB.getConexion().prepareStatement("SELECT * FROM test_resultados WHERE test_id = ?")) {

            pst.setInt(1, testId);

            try (ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {
                    resultados.add(mapearTestResultado(rs));
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar resultados por test: " + testId + " : " + e);
        }

        return resultados;
    }

    @Override
    public Test_Resultados buscarPorUsuarioYTest(Integer usuarioId, Integer testId) {
        Test_Resultados resultado = null;

        try (PreparedStatement pst = conDB.getConexion().prepareStatement
                ("SELECT * FROM test_resultados WHERE usuario_id = ? AND test_id = ?")) {

            pst.setInt(1, usuarioId);
            pst.setInt(2, testId);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    resultado = mapearTestResultado(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException
                    ("Error al buscar resultado por usuario y test: " + usuarioId + ", " + testId + " : " + e);
        }

        return resultado;
    }
}