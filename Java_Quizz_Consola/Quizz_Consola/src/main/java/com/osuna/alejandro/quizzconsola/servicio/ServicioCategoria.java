package com.osuna.alejandro.quizzconsola.servicio;

import com.osuna.alejandro.quizzconsola.dao.implementaciones.CategoriaDAOImpl;
import com.osuna.alejandro.quizzconsola.dao.interfaces.CategoriaDAO;
import com.osuna.alejandro.quizzconsola.modelos.Categorias;

import java.util.List;

public class ServicioCategoria {

    private CategoriaDAO categoriaDAO;

    public ServicioCategoria() {
        this.categoriaDAO = new CategoriaDAOImpl();
    }

    // CRUD
    public boolean crearCategoria(String nombre, String descripcion) {
        Categorias nuevaCategoria = new Categorias(null, nombre, descripcion);
        return categoriaDAO.insertar(nuevaCategoria);
    }

    public Categorias buscarPorId(Integer id) {
        return categoriaDAO.buscarPorId(id);
    }

    public Categorias buscarPorNombre(String nombre) {
        return categoriaDAO.buscarPorNombre(nombre);
    }

    public List<Categorias> obtenerTodasLasCategorias() {
        return categoriaDAO.obtenerTodas();
    }

    public boolean actualizarCategoria(Categorias categoria) {
        return categoriaDAO.actualizar(categoria);
    }

    public boolean eliminarCategoria(Integer id) {
        return categoriaDAO.eliminar(id);
    }

   //------------------------------------------------------------
    public boolean existeCategoria(Integer id) {
        return buscarPorId(id) != null;
    }
}