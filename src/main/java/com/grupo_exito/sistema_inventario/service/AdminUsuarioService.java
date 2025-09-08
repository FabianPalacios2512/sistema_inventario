package com.grupo_exito.sistema_inventario.service;

import com.grupo_exito.sistema_inventario.dto.UsuarioRegistroDTO;
import com.grupo_exito.sistema_inventario.model.entity.Usuario;
import java.util.List;
import java.util.Optional;

public interface AdminUsuarioService {
    List<Usuario> listarTodosLosUsuarios();
    Usuario guardarUsuario(UsuarioRegistroDTO registroDTO);
    void cambiarEstadoUsuario(Integer id); // <-- Este es el cambio
    Optional<Usuario> buscarUsuarioPorId(Integer id);
}