package com.osuna.alejandro.quizzconsola.modelos;

import java.util.Objects;

public class Preguntas_Tags implements Comparable<Preguntas_Tags>{

    private Integer pregunta_id;
    private Integer etiqueta_id;

    public Preguntas_Tags(Integer pregunta_id, Integer etiqueta_id) {
        this.pregunta_id = pregunta_id;
        this.etiqueta_id = etiqueta_id;
    }

    public Integer getPregunta_id() {
        return pregunta_id;
    }

    public void setPregunta_id(Integer pregunta_id) {
        this.pregunta_id = pregunta_id;
    }

    public Integer getEtiqueta_id() {
        return etiqueta_id;
    }

    public void setEtiqueta_id(Integer etiqueta_id) {
        this.etiqueta_id = etiqueta_id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Preguntas_Tags that = (Preguntas_Tags) o;
        return Objects.equals(pregunta_id, that.pregunta_id) && Objects.equals(etiqueta_id, that.etiqueta_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pregunta_id, etiqueta_id);
    }

    @Override
    public String toString() {
        return "Preguntas_Tags{" +
                "pregunta_id=" + pregunta_id +
                ", etiqueta_id=" + etiqueta_id +
                '}';
    }


    @Override
    public int compareTo(Preguntas_Tags o) {
        return pregunta_id.compareTo(o.getPregunta_id());
    }
}
