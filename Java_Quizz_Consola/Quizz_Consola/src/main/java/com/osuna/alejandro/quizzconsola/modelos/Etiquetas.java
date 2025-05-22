package com.osuna.alejandro.quizzconsola.modelos;

import java.util.Objects;

public class Etiquetas implements Comparable<Etiquetas>{

    private Integer id;
    private String etiqueta;

    public Etiquetas(Integer id, String etiqueta) {
        this.id = id;
        this.etiqueta = etiqueta;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Etiquetas etiquetas = (Etiquetas) o;
        return Objects.equals(id, etiquetas.id) && Objects.equals(etiqueta, etiquetas.etiqueta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, etiqueta);
    }

    @Override
    public String toString() {
        return "Etiquetas{" +
                "id=" + id +
                ", etiqueta='" + etiqueta + '\'' +
                '}';
    }


    @Override
    public int compareTo(Etiquetas o) {
        return etiqueta.compareTo(o.getEtiqueta());
    }
}
