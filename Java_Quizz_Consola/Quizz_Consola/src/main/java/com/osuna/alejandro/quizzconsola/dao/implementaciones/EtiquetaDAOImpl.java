package com.osuna.alejandro.quizzconsola.dao.implementaciones;

import com.osuna.alejandro.quizzconsola.dao.ConexionBD;
import com.osuna.alejandro.quizzconsola.dao.interfaces.EtiquetaDAO;
import com.osuna.alejandro.quizzconsola.modelos.Etiquetas;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EtiquetaDAOImpl implements EtiquetaDAO {

    private ConexionBD conDB;

    public EtiquetaDAOImpl() {
        this.conDB = new ConexionBD();
    }

    // Metodo de mapeo
    private Etiquetas mapearEtiqueta(ResultSet rs) throws SQLException {
        return new Etiquetas(
                rs.getInt("id"),
                rs.getString("etiqueta")
        );
    }

    private void validarEtiquetaDuplicada(Etiquetas etiqueta) {
        Etiquetas existente = buscarPorNombre(etiqueta.getEtiqueta());

        if (existente != null && !existente.getId().equals(etiqueta.getId())) {
            throw new RuntimeException("Ya existe una etiqueta con el nombre: " + etiqueta.getEtiqueta());
        }
    }

    private void validarEtiquetaExiste(Integer id) {
        if (buscarPorId(id) == null) {
            throw new RuntimeException("Etiqueta con ID " + id + " no existe");
        }
    }

    @Override
    public Etiquetas buscarPorId(Integer id) {
        Etiquetas etiqueta = null;

        try (PreparedStatement pst = conDB.getConexion().prepareStatement
                ("SELECT * FROM etiquetas WHERE id = ?")) {

            pst.setInt(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    etiqueta = mapearEtiqueta(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar etiqueta por ID: " + id + " : " + e);
        }

        return etiqueta;
    }

    @Override
    public Etiquetas buscarPorNombre(String etiqueta) {
        Etiquetas etiq = null;

        try (PreparedStatement pst = conDB.getConexion().prepareStatement
                ("SELECT * FROM etiquetas WHERE etiqueta = ?")) {

            pst.setString(1, etiqueta);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    etiq = mapearEtiqueta(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar etiqueta por nombre: " + etiqueta + " : " + e);
        }

        return etiq;
    }

    @Override
    public boolean insertar(Etiquetas etiqueta) {
        // Verificar que no existe duplicado
        validarEtiquetaDuplicada(etiqueta);

        try (PreparedStatement pst = conDB.getConexion().prepareStatement(
                "INSERT INTO etiquetas (etiqueta) VALUES (?)")) {

            pst.setString(1, etiqueta.getEtiqueta());

            int filasAfectadas = pst.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar etiqueta: " + etiqueta.getEtiqueta() + " : " + e);
        }
    }

    @Override
    public boolean actualizar(Etiquetas etiqueta) {
        // Verificar que existe
        validarEtiquetaExiste(etiqueta.getId());

        // Verificar duplicados
        validarEtiquetaDuplicada(etiqueta);

        try (PreparedStatement pst = conDB.getConexion().prepareStatement(
                "UPDATE etiquetas SET etiqueta = ? WHERE id = ?")) {

            pst.setString(1, etiqueta.getEtiqueta());
            pst.setInt(2, etiqueta.getId());

            int filasAfectadas = pst.executeUpdate();
            return filasAfectadas == 1;

        } catch (SQLException e) {
            throw new RuntimeException("No se pudo actualizar la etiqueta con ID: " + etiqueta.getId() + " : " + e);
        }
    }

    @Override
    public boolean eliminar(Integer id) {
        // Verificar que existe
        validarEtiquetaExiste(id);

        try (PreparedStatement pst = conDB.getConexion().prepareStatement("DELETE FROM etiquetas WHERE id = ?")) {

            pst.setInt(1, id);

            int filasAfectadas = pst.executeUpdate();
            return filasAfectadas == 1;

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar etiqueta con ID: " + id + " : " + e);
        }
    }

    @Override
    public List<Etiquetas> obtenerTodas() {
        List<Etiquetas> etiquetas = new ArrayList<>();

        try (PreparedStatement pst = conDB.getConexion().prepareStatement("SELECT * FROM etiquetas")) {

            try (ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {
                    etiquetas.add(mapearEtiqueta(rs));
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todas las etiquetas: " + e);
        }

        return etiquetas;
    }
}