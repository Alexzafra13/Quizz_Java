package com.osuna.alejandro.quizzconsola.modelos;

import java.time.LocalDate;
import java.util.Objects;

public class Test_Resultados implements Comparable<Test_Resultados>{

    private Integer id;
    private Integer usuario_id;
    private Integer test_id;
    private Double puntuacion;
    private LocalDate inicio;
    private LocalDate fin;

    public Test_Resultados(Integer id, Integer usuario_id, Integer test_id, Double puntiacion, LocalDate inicio, LocalDate fin) {
        this.id = id;
        this.usuario_id = usuario_id;
        this.test_id = test_id;
        this.puntuacion = puntiacion;
        this.inicio = inicio;
        this.fin = fin;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(Integer usuario_id) {
        this.usuario_id = usuario_id;
    }

    public Integer getTest_id() {
        return test_id;
    }

    public void setTest_id(Integer test_id) {
        this.test_id = test_id;
    }

    public Double getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Double puntuacion) {
        this.puntuacion = puntuacion;
    }

    public LocalDate getInicio() {
        return inicio;
    }

    public void setInicio(LocalDate inicio) {
        this.inicio = inicio;
    }

    public LocalDate getFin() {
        return fin;
    }

    public void setFin(LocalDate fin) {
        this.fin = fin;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Test_Resultados that = (Test_Resultados) o;
        return Objects.equals(id, that.id) && Objects.equals(usuario_id, that.usuario_id) && Objects.equals(test_id, that.test_id) && Objects.equals(puntuacion, that.puntuacion) && Objects.equals(inicio, that.inicio) && Objects.equals(fin, that.fin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, usuario_id, test_id, puntuacion, inicio, fin);
    }

    @Override
    public String toString() {
        return "Test_Resultados{" +
                "id=" + id +
                ", usuario_id=" + usuario_id +
                ", test_id=" + test_id +
                ", puntuacion=" + puntuacion +
                ", inicio=" + inicio +
                ", fin=" + fin +
                '}';
    }


    @Override
    public int compareTo(Test_Resultados o) {
        return puntuacion.compareTo(o.getPuntuacion());
    }
}
