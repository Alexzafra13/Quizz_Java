
package com.osuna.alejandro.quizzconsola.dao.implementaciones;

import com.osuna.alejandro.quizzconsola.dao.ConexionBD;
import com.osuna.alejandro.quizzconsola.dao.interfaces.TestDAO;
import com.osuna.alejandro.quizzconsola.modelos.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TestDAOImpl implements TestDAO {

    private ConexionBD conDB;

    public TestDAOImpl() {
        this.conDB = new ConexionBD();
    }

    // Metodo de mapeo
    private Test mapearTest(ResultSet rs) throws SQLException {
        return new Test(
                rs.getInt("id"),
                rs.getString("titulo"),
                rs.getString("descripcion"),
                rs.getInt("created_by"),
                rs.getDate("created_at").toLocalDate()
        );
    }

    private void validarTestExiste(Integer id) {
        if (buscarPorId(id) == null) {
            throw new RuntimeException("Test con ID " + id + " no existe");
        }
    }

    @Override
    public Test buscarPorId(Integer id) {
        Test test = null;

        try (PreparedStatement pst = conDB.getConexion().prepareStatement("SELECT * FROM test WHERE id = ?")) {

            pst.setInt(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    test = mapearTest(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar test por ID: " + id + " : " + e);
        }

        return test;
    }

    @Override
    public boolean insertar(Test test) {
        try (PreparedStatement pst = conDB.getConexion().prepareStatement(
                "INSERT INTO test (titulo, descripcion, created_by, created_at) VALUES (?, ?, ?, ?)")) {

            pst.setString(1, test.getTitulo());
            pst.setString(2, test.getDescripcion());
            pst.setInt(3, test.getCreated_by());
            pst.setDate(4, java.sql.Date.valueOf(test.getCreated_at()));

            int filasAfectadas = pst.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar test: " + e);
        }
    }

    @Override
    public boolean actualizar(Test test) {
        // Verificar que existe
        validarTestExiste(test.getId());

        try (PreparedStatement pst = conDB.getConexion().prepareStatement(
                "UPDATE test SET titulo = ?, descripcion = ?, created_by = ?, created_at = ? WHERE id = ?")) {

            pst.setString(1, test.getTitulo());
            pst.setString(2, test.getDescripcion());
            pst.setInt(3, test.getCreated_by());
            pst.setDate(4, java.sql.Date.valueOf(test.getCreated_at()));
            pst.setInt(5, test.getId());

            int filasAfectadas = pst.executeUpdate();
            return filasAfectadas == 1;

        } catch (SQLException e) {
            throw new RuntimeException("No se pudo actualizar el test con ID: " + test.getId() + " : " + e);
        }
    }

    @Override
    public boolean eliminar(Integer id) {
        // Verificar que existe
        validarTestExiste(id);

        try (PreparedStatement pst = conDB.getConexion().prepareStatement("DELETE FROM test WHERE id = ?")) {

            pst.setInt(1, id);

            int filasAfectadas = pst.executeUpdate();
            return filasAfectadas == 1;

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar test con ID: " + id + " : " + e);
        }
    }

    @Override
    public List<Test> obtenerTodos() {
        List<Test> tests = new ArrayList<>();

        try (PreparedStatement pst = conDB.getConexion().prepareStatement("SELECT * FROM test")) {

            try (ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {
                    tests.add(mapearTest(rs));
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todos los tests: " + e);
        }

        return tests;
    }
}