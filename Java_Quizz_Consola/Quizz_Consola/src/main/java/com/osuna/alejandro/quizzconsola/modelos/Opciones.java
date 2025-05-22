package com.osuna.alejandro.quizzconsola.modelos;

import java.util.Objects;

public class Opciones implements Comparable<Opciones>{

    private Integer id;
    private Integer preguntas_id;
    private String opcion_text;
    private boolean is_correcta;

    public Opciones(Integer id, Integer preguntas_id, String opcion_text, boolean is_correcta) {
        this.id = id;
        this.preguntas_id = preguntas_id;
        this.opcion_text = opcion_text;
        this.is_correcta = is_correcta;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPreguntas_id() {
        return preguntas_id;
    }

    public void setPreguntas_id(Integer preguntas_id) {
        this.preguntas_id = preguntas_id;
    }

    public String getOpcion_text() {
        return opcion_text;
    }

    public void setOpcion_text(String opcion_text) {
        this.opcion_text = opcion_text;
    }

    public boolean isIs_correcta() {
        return is_correcta;
    }

    public void setIs_correcta(boolean is_correcta) {
        this.is_correcta = is_correcta;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Opciones opciones = (Opciones) o;
        return is_correcta == opciones.is_correcta && Objects.equals(id, opciones.id) && Objects.equals(preguntas_id, opciones.preguntas_id) && Objects.equals(opcion_text, opciones.opcion_text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, preguntas_id, opcion_text, is_correcta);
    }

    @Override
    public String toString() {
        return "Opciones{" +
                "id=" + id +
                ", preguntas_id=" + preguntas_id +
                ", opcion_text='" + opcion_text + '\'' +
                ", is_correcta=" + is_correcta +
                '}';
    }

    @Override
    public int compareTo(Opciones o) {
        return preguntas_id.compareTo(o.getPreguntas_id());
    }
}
