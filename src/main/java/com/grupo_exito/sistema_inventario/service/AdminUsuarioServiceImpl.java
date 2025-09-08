package com.grupo_exito.sistema_inventario.service;

import com.grupo_exito.sistema_inventario.dto.UsuarioRegistroDTO;
import com.grupo_exito.sistema_inventario.model.entity.Rol;
import com.grupo_exito.sistema_inventario.model.entity.Usuario;
import com.grupo_exito.sistema_inventario.model.repository.RolRepository;
import com.grupo_exito.sistema_inventario.model.repository.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AdminUsuarioServiceImpl implements AdminUsuarioService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio; // Usando tu repositorio

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Usuario> listarTodosLosUsuarios() {
        return usuarioRepositorio.findAll();
    }

    @Override
    public Usuario guardarUsuario(UsuarioRegistroDTO registroDTO) {
        Usuario usuario;

        if (registroDTO.getId() != null) {
            // Edición de usuario existente
            usuario = usuarioRepositorio.findById(registroDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + registroDTO.getId()));
        } else {
            // Creación de nuevo usuario
            usuario = new Usuario();
            usuario.setActivo(true);
            usuario.setFechaCreacion(LocalDateTime.now());
        }

        // Mapeando DTO a la entidad con tus nombres de campo
        usuario.setNombre(registroDTO.getNombre());
        usuario.setApellido(registroDTO.getApellido());
        usuario.setCorreo(registroDTO.getCorreo());

        // Actualizar contraseña solo si se proporciona una nueva
        if (registroDTO.getPassword() != null && !registroDTO.getPassword().isEmpty()) {
            usuario.setContrasenaHash(passwordEncoder.encode(registroDTO.getPassword()));
        }

        // Asignar roles (usando ID de tipo Integer)
        if (registroDTO.getRolesIds() != null && !registroDTO.getRolesIds().isEmpty()) {
            Set<Rol> roles = registroDTO.getRolesIds().stream()
                    .map(rolId -> rolRepository.findById(rolId).orElse(null))
                    .filter(rol -> rol != null)
                    .collect(Collectors.toSet());
            usuario.setRoles(roles);
        } else {
            usuario.setRoles(new HashSet<>());
        }

        return usuarioRepositorio.save(usuario);
    }

    @Override
    public void eliminarUsuario(Integer id) {
        usuarioRepositorio.deleteById(id);
    }

    @Override
    public Optional<Usuario> buscarUsuarioPorId(Integer id) {
        return usuarioRepositorio.findById(id);
    }
}