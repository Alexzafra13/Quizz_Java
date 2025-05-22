package com.osuna.alejandro.quizzconsola.modelos;

import java.time.LocalDate;
import java.util.Objects;

public class Usuario_Respuesta implements Comparable<Usuario_Respuesta>{

    private Integer id;
    private Integer usuario_id;
    private Integer test_id;
    private Integer pregunta_id;
    private Integer seleccionada_opcion;
    private LocalDate tiempo_respuesta;

    public Usuario_Respuesta(Integer id, Integer usuario_id, Integer test_id, Integer pregunta_id, Integer seleccionada_opcion, LocalDate tiempo_respuesta) {
        this.id = id;
        this.usuario_id = usuario_id;
        this.test_id = test_id;
        this.pregunta_id = pregunta_id;
        this.seleccionada_opcion = seleccionada_opcion;
        this.tiempo_respuesta = tiempo_respuesta;
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

    public Integer getPregunta_id() {
        return pregunta_id;
    }

    public void setPregunta_id(Integer pregunta_id) {
        this.pregunta_id = pregunta_id;
    }

    public Integer getSeleccionada_opcion() {
        return seleccionada_opcion;
    }

    public void setSeleccionada_opcion(Integer seleccionada_opcion) {
        this.seleccionada_opcion = seleccionada_opcion;
    }

    public LocalDate getTiempo_respuesta() {
        return tiempo_respuesta;
    }

    public void setTiempo_respuesta(LocalDate tiempo_respuesta) {
        this.tiempo_respuesta = tiempo_respuesta;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Usuario_Respuesta that = (Usuario_Respuesta) o;
        return Objects.equals(id, that.id) && Objects.equals(usuario_id, that.usuario_id) && Objects.equals(test_id, that.test_id) && Objects.equals(pregunta_id, that.pregunta_id) && Objects.equals(seleccionada_opcion, that.seleccionada_opcion) && Objects.equals(tiempo_respuesta, that.tiempo_respuesta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, usuario_id, test_id, pregunta_id, seleccionada_opcion, tiempo_respuesta);
    }

    @Override
    public String toString() {
        return "Usuario_Respuesta{" +
                "id=" + id +
                ", usuario_id=" + usuario_id +
                ", test_id=" + test_id +
                ", pregunta_id=" + pregunta_id +
                ", seleccionada_opcion=" + seleccionada_opcion +
                ", tiempo_respuesta=" + tiempo_respuesta +
                '}';
    }

    @Override
    public int compareTo(Usuario_Respuesta o) {
        return usuario_id.compareTo(o.getUsuario_id());
    }
}
