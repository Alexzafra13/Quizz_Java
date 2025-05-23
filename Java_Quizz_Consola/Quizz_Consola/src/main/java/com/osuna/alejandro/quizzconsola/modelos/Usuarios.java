package com.osuna.alejandro.quizzconsola.modelos;

import com.osuna.alejandro.quizzconsola.modelos.enums.Rol;

import java.time.LocalDate;
import java.util.Objects;

public class Usuarios  implements Comparable<Usuarios>{

    private Integer id;
    private String username;
    private String email;
    private String password_hash;
    private Rol role;
    private LocalDate created_at;

    public Usuarios(Integer id, String username, String email, String password_hash, Rol role, LocalDate created_at) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password_hash = password_hash;
        this.role = role;
        this.created_at = created_at;
    }
    //Constructor para inserciones o actualizaciones a base de datos dado que el id es autoincremental y no se debe insertar
    public Usuarios(String username, String email, String password_hash, Rol role, LocalDate created_at) {
        this.id = null;
        this.username = username;
        this.email = email;
        this.password_hash = password_hash;
        this.role = role;
        this.created_at = created_at;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public Rol getRole() {
        return role;
    }

    public void setRole(Rol role) {
        this.role = role;
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
        Usuarios usuarios = (Usuarios) o;
        return Objects.equals(id, usuarios.id) && Objects.equals(username, usuarios.username) &&
                Objects.equals(email, usuarios.email) && Objects.equals(password_hash, usuarios.password_hash) &&
                role == usuarios.role && Objects.equals(created_at, usuarios.created_at);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, password_hash, role, created_at);
    }

    @Override
    public String toString() {
        return "Usuarios{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password_hash='" + password_hash + '\'' +
                ", role=" + role +
                ", created_at=" + created_at +
                '}';
    }

    @Override
    public int compareTo(Usuarios o){

        return username.compareTo(o.getUsername());
    }


}
