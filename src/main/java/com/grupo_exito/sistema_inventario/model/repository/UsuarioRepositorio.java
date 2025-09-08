package com.grupo_exito.sistema_inventario.model.repository; // <-- PAQUETE ACTUALIZADO

import com.grupo_exito.sistema_inventario.model.entity.Usuario; // <-- IMPORT ACTUALIZADO
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Integer> {
    
    Optional<Usuario> findByCorreo(String correo);
    Optional<Usuario> findByResetPasswordToken(String token);
}