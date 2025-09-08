package com.grupo_exito.sistema_inventario.model.entity; // <-- PAQUETE ACTUALIZADO

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Rol {
    // ... el contenido de la clase no cambia ...
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Integer id;

    @Column(name = "nombre_rol")
    private String nombre;

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}