package com.grupo_exito.sistema_inventario.model.repository;

import com.grupo_exito.sistema_inventario.model.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Integer> {
    
    Optional<Rol> findByNombre(String nombre);
}
