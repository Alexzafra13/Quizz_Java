package com.osuna.alejandro.quizzconsola.dao.implementaciones;

import com.osuna.alejandro.quizzconsola.dao.ConexionBD;
import com.osuna.alejandro.quizzconsola.dao.interfaces.UsuarioDAO;
import com.osuna.alejandro.quizzconsola.modelos.Usuarios;
import com.osuna.alejandro.quizzconsola.modelos.enums.Rol;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAOImpl implements UsuarioDAO {

    private ConexionBD conDB;

    public UsuarioDAOImpl() {
        this.conDB = new ConexionBD();
    }

    // Aqui mapeo el usuario completo para no tener que reescribir el codigo en busquedas iguales
    private Usuarios mapearUsuario(ResultSet rs) throws SQLException {
        return new Usuarios(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("password_hash"),
                Rol.valueOf(rs.getString("rol")),
                rs.getDate("created_at").toLocalDate()
        );
    }

    private void comprobacionDuplicidadUsuario(Usuarios usuario){
        // Verifico si email ya existe en otro usuario
        Usuarios usuarioConEmail = buscarPorEmail(usuario.getEmail());

        if (usuarioConEmail != null && !usuarioConEmail.getId().equals(usuario.getId())) {
            throw new RuntimeException("Email " + usuario.getEmail() + " ya está en uso por otro usuario");
        }

        // Verifico si username ya existe en otro usuario
        Usuarios usuarioConUsername = buscarPorUsername(usuario.getUsername());

        if (usuarioConUsername != null && !usuarioConUsername.getId().equals(usuario.getId())) {
            throw new RuntimeException("Username " + usuario.getUsername() + " ya está en uso por otro usuario");
        }
    }

    @Override
    public Usuarios buscarPorEmail(String email) {

        Usuarios user_email = null;

        try {
            String sql = "SELECT * FROM usuarios WHERE email = ?";

            PreparedStatement pst = conDB.getConexion().prepareStatement(sql);

            pst.setString(1, email);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                user_email = mapearUsuario(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar el usuario por el mail: " + email + " : " + e);
        }

        return user_email;
    }

    @Override
    public Usuarios buscarPorUsername(String username) {

        Usuarios user_name = null;

        try {
            String sql = "SELECT * FROM usuarios WHERE username = ?";

            PreparedStatement pst = conDB.getConexion().prepareStatement(sql);

            pst.setString(1, username);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                user_name = mapearUsuario(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario por username: " + username + " : " + e);
        }

        return user_name;
    }

    @Override
    public Usuarios buscarPorId(Integer id) {

        Usuarios user_id = null;

        try {
            String sql = "SELECT * FROM usuarios WHERE id = ?";

            PreparedStatement pst = conDB.getConexion().prepareStatement(sql);

            pst.setInt(1, id);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                user_id  = mapearUsuario(rs);
            }

        } catch (SQLException e) {

            throw new RuntimeException("Error al buscar usuario por ID: " + id + " : " + e);
        }

        return user_id;
    }

    @Override
    public boolean insertarUsuario(Usuarios usuario) {

        // Compruebo si existe duplicidad con el nombre o el email en la base de datos
        comprobacionDuplicidadUsuario(usuario);

        try {
            String sql = "INSERT INTO usuarios (username, email, password_hash, rol, created_at) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement pst = conDB.getConexion().prepareStatement(sql);

            pst.setString(1, usuario.getUsername());
            pst.setString(2, usuario.getEmail());
            pst.setString(3, usuario.getPassword_hash());
            pst.setString(4, usuario.getRole().toString());
            pst.setDate(5, java.sql.Date.valueOf(usuario.getCreated_at()));

            int filasAfectadas = pst.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar usuario: " + e);
        }
    }

    @Override
    public boolean actualizarUsuario(Usuarios usuario) {

        // Verifico que el usuario existe usando buscarPorId()
        if (buscarPorId(usuario.getId()) == null) {

            throw new RuntimeException("No se puede actualizar: usuario con ID " + usuario.getId() + " no existe");
        }

        comprobacionDuplicidadUsuario(usuario);

        try {
            String sql = "UPDATE usuarios SET username = ?, email = ?, password_hash = ?, rol = ?, created_at = ? WHERE id = ?";

            PreparedStatement pst = conDB.getConexion().prepareStatement(sql);

            pst.setString(1, usuario.getUsername());
            pst.setString(2, usuario.getEmail());
            pst.setString(3, usuario.getPassword_hash());
            pst.setString(4, usuario.getRole().toString());
            pst.setDate(5, java.sql.Date.valueOf(usuario.getCreated_at()));
            pst.setInt(6, usuario.getId());

            int filasAfectadas = pst.executeUpdate();

            return filasAfectadas == 1;

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar usuario con ID: " + usuario.getId() + " : " + e);
        }
    }

    @Override
    public boolean eliminarUsuarioId(Integer id) {

        // Verifico que el usuario existe usando buscarPorId()
        if (buscarPorId(id) == null) {
            throw new RuntimeException("No se puede eliminar: usuario con ID " + id + " no existe");
        }

        try {
            String sql = "DELETE FROM usuarios WHERE id = ?";

            PreparedStatement pst = conDB.getConexion().prepareStatement(sql);
            pst.setInt(1, id);

            int filasAfectadas = pst.executeUpdate();
            return filasAfectadas == 1;

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar usuario con ID: " + id + " : " +  e);
        }
    }

    @Override
    public boolean eliminarUsuarioNombre(String username) {

        if (buscarPorUsername(username) == null){

            throw new RuntimeException("No se puede eliminar: usuario con nombre " + username + " no existe");
        }

        try {
            String sql = "DELETE FROM usuarios WHERE username = ?";

            PreparedStatement pst = conDB.getConexion().prepareStatement(sql);
            pst.setString(1, username);

            int filasAfectadas = pst.executeUpdate();
            return filasAfectadas == 1;

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar usuario con nombre: " + username + " : " +  e);
        }

    }

    @Override
    public boolean eliminarUsuarioEmail(String email) {

        if (buscarPorEmail(email) == null){

            throw new RuntimeException("No se puede eliminar: usuario con email " + email + " no existe");
        }



        return false;
    }

    @Override
    public List<Usuarios> obtenerTodosLosUsuarios() {
        List<Usuarios> usuarios = new ArrayList<>();

        try {
            String sql = "SELECT * FROM usuarios";

            PreparedStatement pst = conDB.getConexion().prepareStatement(sql);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todos los usuarios: " + e);
        }

        return usuarios;
    }

}