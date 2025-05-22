package com.osuna.alejandro.quizzconsola.modelos;

import java.util.Objects;

public class Categorias  implements Comparable<Categorias>{

    private Integer id;
    private String categoria;
    private String descripcion;

    public Categorias(Integer id, String categoria, String descripcion) {
        this.id = id;
        this.categoria = categoria;
        this.descripcion = descripcion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Categorias that = (Categorias) o;
        return Objects.equals(id, that.id) && Objects.equals(categoria, that.categoria) && Objects.equals(descripcion, that.descripcion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, categoria, descripcion);
    }

    @Override
    public String toString() {
        return "Categorias{" +
                "id=" + id +
                ", categoria='" + categoria + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }

    @Override
    public int compareTo(Categorias o) {
        return categoria.compareTo(o.getCategoria());
    }
}
