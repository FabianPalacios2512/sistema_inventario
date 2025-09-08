package com.grupo_exito.sistema_inventario.dto;

import java.util.List;

public class UsuarioRegistroDTO {
    private Integer id;
    private String nombre;
    private String apellido;
    private String correo;
    private String password; // Para la nueva contraseña
    private List<Integer> rolesIds;

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public List<Integer> getRolesIds() { return rolesIds; }
    public void setRolesIds(List<Integer> rolesIds) { this.rolesIds = rolesIds; }
}