
package com.osuna.alejandro.quizzconsola.dao.implementaciones;

import com.osuna.alejandro.quizzconsola.dao.ConexionBD;
import com.osuna.alejandro.quizzconsola.dao.interfaces.PreguntasTagsDAO;
import com.osuna.alejandro.quizzconsola.modelos.Preguntas_Tags;
import com.osuna.alejandro.quizzconsola.modelos.Etiquetas;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PreguntasTagsDAOImpl implements PreguntasTagsDAO {

    private ConexionBD conDB;

    public PreguntasTagsDAOImpl() {
        this.conDB = new ConexionBD();
    }

    // Metodo de mapeo para etiquetas
    private Etiquetas mapearEtiqueta(ResultSet rs) throws SQLException {
        return new Etiquetas(
                rs.getInt("id"),
                rs.getString("etiqueta")
        );
    }

    private boolean existeRelacion(Integer preguntaId, Integer etiquetaId) {
        try (PreparedStatement pst = conDB.getConexion().prepareStatement(
                "SELECT COUNT(*) FROM preguntas_tags WHERE pregunta_id = ? AND etiqueta_id = ?")) {

            pst.setInt(1, preguntaId);
            pst.setInt(2, etiquetaId);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar relación pregunta-etiqueta: " + e);
        }

        return false;
    }

    @Override
    public boolean insertar(Preguntas_Tags preguntaTag) {
        // Verificar que no existe ya la relación
        if (existeRelacion(preguntaTag.getPregunta_id(), preguntaTag.getEtiqueta_id())) {

            throw new RuntimeException("La relación entre pregunta " + preguntaTag.getPregunta_id() +
                    " y etiqueta " + preguntaTag.getEtiqueta_id() + " ya existe");
        }

        try (PreparedStatement pst = conDB.getConexion().prepareStatement(
                "INSERT INTO preguntas_tags (pregunta_id, etiqueta_id) VALUES (?, ?)")) {

            pst.setInt(1, preguntaTag.getPregunta_id());
            pst.setInt(2, preguntaTag.getEtiqueta_id());

            int filasAfectadas = pst.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar relación pregunta-etiqueta: " + e);
        }
    }

    @Override
    public boolean eliminar(Integer preguntaId, Integer etiquetaId) {

        // Verificar que existe la relación
        if (!existeRelacion(preguntaId, etiquetaId)) {

            throw new RuntimeException("La relación entre pregunta " + preguntaId +
                    " y etiqueta " + etiquetaId + " no existe");
        }

        try (PreparedStatement pst = conDB.getConexion().prepareStatement(
                "DELETE FROM preguntas_tags WHERE pregunta_id = ? AND etiqueta_id = ?")) {

            pst.setInt(1, preguntaId);
            pst.setInt(2, etiquetaId);

            int filasAfectadas = pst.executeUpdate();
            return filasAfectadas == 1;

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar relación pregunta-etiqueta: " + e);
        }
    }

    @Override
    public List<Etiquetas> obtenerEtiquetasPorPregunta(Integer preguntaId) {
        List<Etiquetas> etiquetas = new ArrayList<>();

        try (PreparedStatement pst = conDB.getConexion().prepareStatement(
                "SELECT e.* FROM etiquetas e " +
                        "INNER JOIN preguntas_tags pt ON e.id = pt.etiqueta_id " +
                        "WHERE pt.pregunta_id = ?")) {

            pst.setInt(1, preguntaId);

            try (ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {
                    etiquetas.add(mapearEtiqueta(rs));
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener etiquetas por pregunta: " + preguntaId + " : " + e);
        }

        return etiquetas;
    }

    @Override
    public List<Integer> obtenerPreguntasPorEtiqueta(Integer etiquetaId) {
        List<Integer> preguntaIds = new ArrayList<>();

        try (PreparedStatement pst = conDB.getConexion().prepareStatement(
                "SELECT pregunta_id FROM preguntas_tags WHERE etiqueta_id = ?")) {

            pst.setInt(1, etiquetaId);

            try (ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {
                    preguntaIds.add(rs.getInt("pregunta_id"));
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener preguntas por etiqueta: " + etiquetaId + " : " + e);
        }

        return preguntaIds;
    }
}