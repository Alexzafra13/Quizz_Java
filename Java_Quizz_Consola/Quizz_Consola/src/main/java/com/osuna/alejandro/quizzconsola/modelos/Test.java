package com.osuna.alejandro.quizzconsola.modelos;

import java.time.LocalDate;
import java.util.Objects;

public class Test implements Comparable<Test>{

    private Integer id;
    private String titulo;
    private String descripcion;
    private Integer created_by;
    private LocalDate created_at;

    public Test(Integer id, String titulo, String descripcion, Integer created_by, LocalDate created_at) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.created_by = created_by;
        this.created_at = created_at;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCreated_by() {
        return created_by;
    }

    public void setCreated_by(Integer created_by) {
        this.created_by = created_by;
    }

    public LocalDate getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDate created_at) {
        this.created_at = created_at;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Test test = (Test) o;
        return Objects.equals(id, test.id) && Objects.equals(titulo, test.titulo) && Objects.equals(descripcion, test.descripcion) && Objects.equals(created_by, test.created_by) && Objects.equals(created_at, test.created_at);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titulo, descripcion, created_by, created_at);
    }

    @Override
    public String toString() {
        return "Test{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", created_by=" + created_by +
                ", created_at=" + created_at +
                '}';
    }

    @Override
    public int compareTo(Test o) {
        return titulo.compareTo(o.getTitulo());
    }
}
