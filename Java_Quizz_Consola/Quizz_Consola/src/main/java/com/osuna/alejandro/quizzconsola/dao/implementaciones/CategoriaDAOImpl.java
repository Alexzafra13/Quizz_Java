
package com.osuna.alejandro.quizzconsola.dao.implementaciones;

import com.osuna.alejandro.quizzconsola.dao.ConexionBD;
import com.osuna.alejandro.quizzconsola.dao.interfaces.CategoriaDAO;
import com.osuna.alejandro.quizzconsola.modelos.Categorias;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAOImpl implements CategoriaDAO {

    private ConexionBD conDB;

    public CategoriaDAOImpl() {
        this.conDB = new ConexionBD();
    }

    // Metodo de mapeo
    private Categorias mapearCategoria(ResultSet rs) throws SQLException {
        return new Categorias(
                rs.getInt("id"),
                rs.getString("categoria"),
                rs.getString("descripcion")
        );
    }

    private void validarCategoriaDuplicada(Categorias categoria) {
        Categorias existente = buscarPorNombre(categoria.getCategoria());

        if (existente != null && !existente.getId().equals(categoria.getId())) {
            throw new RuntimeException("Ya existe una categoría con el nombre: " + categoria.getCategoria());
        }
    }

    private void validarCategoriaExiste(Integer id) {
        if (buscarPorId(id) == null) {
            throw new RuntimeException("Categoría con ID " + id + " no existe");
        }
    }

    @Override
    public Categorias buscarPorId(Integer id) {

        Categorias cat = null;

        try (PreparedStatement pst = conDB.getConexion().prepareStatement("SELECT * FROM categorias WHERE id = ?")) {

            pst.setInt(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    cat = mapearCategoria(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar categoría por ID: " + id + " : " + e);
        }

        return cat;
    }

    @Override
    public Categorias buscarPorNombre(String categoria) {
        Categorias cat = null;

        try(PreparedStatement pst = conDB.getConexion().prepareStatement("SELECT * FROM categorias WHERE categoria = ?")){

            pst.setString(1, categoria);

            try (ResultSet rs = pst.executeQuery()){
                if (rs.next()){
                    cat = mapearCategoria(rs);
                }

            }

        }catch (SQLException e){
            throw new RuntimeException("Error al buscar categoria por Nombre_Categoria: " + categoria + " : " + e);
        }

        return cat;
    }

    @Override
    public boolean insertar(Categorias categoria) {

        // Verifico que no existe duplicado
        validarCategoriaDuplicada(categoria);

        try (PreparedStatement pst = conDB.getConexion().prepareStatement
                ("INSERT INTO categorias (categoria, descripcion) VALUES (?, ?)")) {

            pst.setString(1, categoria.getCategoria());
            pst.setString(2, categoria.getDescripcion());

            int filasAfectadas = pst.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar una nueva categoria con el nombre " + categoria.getCategoria() + " : " + e);
        }
    }

    @Override
    public boolean actualizar(Categorias categoria) {

        // Verifico que existe
        validarCategoriaExiste(categoria.getId());

        // Verifico si hay duplicados
        validarCategoriaDuplicada(categoria);

        try (PreparedStatement pst = conDB.getConexion().prepareStatement(
                "UPDATE categorias SET categoria = ?, descripcion = ? WHERE id = ?")) {

            pst.setString(1, categoria.getCategoria());
            pst.setString(2, categoria.getDescripcion());
            pst.setInt(3, categoria.getId());

            int filasAfectadas = pst.executeUpdate();
            return filasAfectadas == 1;

        } catch (SQLException e) {
            throw new RuntimeException("No se pudo actualizar la categoria con ID: " + categoria.getId() + " : " + e);
        }
    }

    @Override
    public boolean eliminar(Integer id) {

        // Verificar que existe
        validarCategoriaExiste(id);

        try (PreparedStatement pst = conDB.getConexion().prepareStatement(
                "DELETE FROM categorias WHERE id = ?")) {

            pst.setInt(1, id);

            int filasAfectadas = pst.executeUpdate();
            return filasAfectadas == 1;

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar categoria con ID: " + id + " : " + e);
        }
    }

    @Override
    public List<Categorias> obtenerTodas() {

        List<Categorias> lista_cat = new ArrayList<>();

        try (PreparedStatement pst = conDB.getConexion().prepareStatement("SELECT * FROM categorias")) {

            try (ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {
                    lista_cat.add(mapearCategoria(rs));
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todas las categorias: " + e);
        }

        return lista_cat;
    }

}