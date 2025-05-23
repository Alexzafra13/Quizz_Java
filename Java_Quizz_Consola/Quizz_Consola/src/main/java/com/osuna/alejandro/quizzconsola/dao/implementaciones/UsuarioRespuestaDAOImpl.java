package com.osuna.alejandro.quizzconsola.dao.implementaciones;

import com.osuna.alejandro.quizzconsola.dao.ConexionBD;
import com.osuna.alejandro.quizzconsola.dao.interfaces.UsuarioRespuestaDAO;
import com.osuna.alejandro.quizzconsola.modelos.Usuario_Respuesta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRespuestaDAOImpl implements UsuarioRespuestaDAO {

    private ConexionBD conDB;

    public UsuarioRespuestaDAOImpl() {
        this.conDB = new ConexionBD();
    }

    // Metodo de mapeo
    private Usuario_Respuesta mapearUsuarioRespuesta(ResultSet rs) throws SQLException {
        return new Usuario_Respuesta(
                rs.getInt("id"),
                rs.getInt("usuario_id"),
                rs.getInt("test_id"),
                rs.getInt("pregunta_id"),
                rs.getInt("seleccionada_opcion_id"),
                rs.getDate("tiempo_respuesta").toLocalDate()
        );
    }

    private void validarUsuarioRespuestaExiste(Integer id) {
        if (buscarPorId(id) == null) {
            throw new RuntimeException("Respuesta de usuario con ID " + id + " no existe");
        }
    }

    @Override
    public Usuario_Respuesta buscarPorId(Integer id) {
        Usuario_Respuesta respuesta = null;

        try (PreparedStatement pst = conDB.getConexion().prepareStatement
                ("SELECT * FROM usuario_respuesta WHERE id = ?")) {

            pst.setInt(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    respuesta = mapearUsuarioRespuesta(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar respuesta por ID: " + id + " : " + e);
        }

        return respuesta;
    }

    @Override
    public boolean insertar(Usuario_Respuesta respuesta) {
        try (PreparedStatement pst = conDB.getConexion().prepareStatement(
                "INSERT INTO usuario_respuesta (usuario_id, test_id, pregunta_id, seleccionada_opcion_id, tiempo_respuesta) VALUES (?, ?, ?, ?, ?)")) {

            pst.setInt(1, respuesta.getUsuario_id());
            pst.setInt(2, respuesta.getTest_id());
            pst.setInt(3, respuesta.getPregunta_id());
            pst.setInt(4, respuesta.getSeleccionada_opcion());
            pst.setDate(5, java.sql.Date.valueOf(respuesta.getTiempo_respuesta()));

            int filasAfectadas = pst.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar respuesta de usuario: " + e);
        }
    }

    @Override
    public boolean actualizar(Usuario_Respuesta respuesta) {
        // Verificar que existe
        validarUsuarioRespuestaExiste(respuesta.getId());

        try (PreparedStatement pst = conDB.getConexion().prepareStatement(
                "UPDATE usuario_respuesta SET usuario_id = ?, test_id = ?, pregunta_id = ?, seleccionada_opcion_id = ?, tiempo_respuesta = ? WHERE id = ?")) {

            pst.setInt(1, respuesta.getUsuario_id());
            pst.setInt(2, respuesta.getTest_id());
            pst.setInt(3, respuesta.getPregunta_id());
            pst.setInt(4, respuesta.getSeleccionada_opcion());
            pst.setDate(5, java.sql.Date.valueOf(respuesta.getTiempo_respuesta()));
            pst.setInt(6, respuesta.getId());

            int filasAfectadas = pst.executeUpdate();
            return filasAfectadas == 1;

        } catch (SQLException e) {
            throw new RuntimeException("No se pudo actualizar la respuesta con ID: " + respuesta.getId() + " : " + e);
        }
    }

    @Override
    public boolean eliminar(Integer id) {
        // Verificar que existe
        validarUsuarioRespuestaExiste(id);

        try (PreparedStatement pst = conDB.getConexion().prepareStatement
                ("DELETE FROM usuario_respuesta WHERE id = ?")) {

            pst.setInt(1, id);

            int filasAfectadas = pst.executeUpdate();
            return filasAfectadas == 1;

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar respuesta con ID: " + id + " : " + e);
        }
    }

    @Override
    public List<Usuario_Respuesta> buscarPorTest(Integer testId, Integer usuarioId) {
        List<Usuario_Respuesta> respuestas = new ArrayList<>();

        try (PreparedStatement pst = conDB.getConexion().prepareStatement
                ("SELECT * FROM usuario_respuesta WHERE test_id = ? AND usuario_id = ?")) {

            pst.setInt(1, testId);
            pst.setInt(2, usuarioId);

            try (ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {
                    respuestas.add(mapearUsuarioRespuesta(rs));
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException
                    ("Error al buscar respuestas por test y usuario: " + testId + ", " + usuarioId + " : " + e);
        }

        return respuestas;
    }

    @Override
    public List<Usuario_Respuesta> buscarPorUsuario(Integer usuarioId) {
        List<Usuario_Respuesta> respuestas = new ArrayList<>();

        try (PreparedStatement pst = conDB.getConexion().prepareStatement
                ("SELECT * FROM usuario_respuesta WHERE usuario_id = ?")) {

            pst.setInt(1, usuarioId);

            try (ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {
                    respuestas.add(mapearUsuarioRespuesta(rs));
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar respuestas por usuario: " + usuarioId + " : " + e);
        }

        return respuestas;
    }
}