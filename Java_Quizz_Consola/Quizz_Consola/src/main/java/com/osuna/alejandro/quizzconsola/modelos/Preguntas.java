package com.osuna.alejandro.quizzconsola.modelos;

import com.osuna.alejandro.quizzconsola.modelos.enums.Dificultad;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

public class Preguntas  implements Comparable<Preguntas>{

    private Integer id;
    private String preguntas_text;
    private Integer categoria_id;
    private Dificultad dificultad;
    private LocalDate create_at;

    public Preguntas(Integer id, String preguntas_text, Integer categoria_id, Dificultad dificultad, LocalDate create_at) {
        this.id = id;
        this.preguntas_text = preguntas_text;
        this.categoria_id = categoria_id;
        this.dificultad = dificultad;
        this.create_at = create_at;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPreguntas_text() {
        return preguntas_text;
    }

    public void setPreguntas_text(String preguntas_text) {
        this.preguntas_text = preguntas_text;
    }

    public Integer getCategoria_id() {
        return categoria_id;
    }

    public void setCategoria_id(Integer categoria_id) {
        this.categoria_id = categoria_id;
    }

    public Dificultad getDificultad() {
        return dificultad;
    }

    public void setDificultad(Dificultad dificultad) {
        this.dificultad = dificultad;
    }

    public LocalDate getCreate_at() {
        return create_at;
    }

    public void setCreate_at(LocalDate create_at) {
        this.create_at = create_at;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Preguntas preguntas = (Preguntas) o;
        return Objects.equals(id, preguntas.id) && Objects.equals(preguntas_text, preguntas.preguntas_text) && Objects.equals(categoria_id, preguntas.categoria_id) && dificultad == preguntas.dificultad && Objects.equals(create_at, preguntas.create_at);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, preguntas_text, categoria_id, dificultad, create_at);
    }

    @Override
    public String toString() {
        return "Preguntas{" +
                "id=" + id +
                ", preguntas_text='" + preguntas_text + '\'' +
                ", categoria_id=" + categoria_id +
                ", dificultad=" + dificultad +
                ", create_at=" + create_at +
                '}';
    }

    @Override
    public int compareTo(Preguntas o) {
        return categoria_id.compareTo(o.categoria_id);
    }
}
