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
    private UsuarioRepositorio usuarioRepositorio;

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
        
        // ===== INICIO DE LA VALIDACIÓN =====
        Optional<Usuario> usuarioExistente = usuarioRepositorio.findByCorreo(registroDTO.getCorreo());
        
        // Si es un usuario nuevo (ID es null) y el correo ya existe, lanza error.
        if (registroDTO.getId() == null && usuarioExistente.isPresent()) {
            throw new RuntimeException("Ya existe un usuario registrado con el correo: " + registroDTO.getCorreo());
        }

        // Si se está editando, pero el correo ya pertenece a OTRO usuario, lanza error.
        if (registroDTO.getId() != null && usuarioExistente.isPresent() && !usuarioExistente.get().getId().equals(registroDTO.getId())) {
             throw new RuntimeException("El correo " + registroDTO.getCorreo() + " ya está en uso por otro usuario.");
        }
        // ===== FIN DE LA VALIDACIÓN =====

        Usuario usuario;
        if (registroDTO.getId() != null) {
            usuario = usuarioRepositorio.findById(registroDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + registroDTO.getId()));
        } else {
            usuario = new Usuario();
            usuario.setActivo(true);
            usuario.setFechaCreacion(LocalDateTime.now());
        }

        usuario.setNombre(registroDTO.getNombre());
        usuario.setApellido(registroDTO.getApellido());
        usuario.setCorreo(registroDTO.getCorreo());

        if (registroDTO.getPassword() != null && !registroDTO.getPassword().isEmpty()) {
            usuario.setContrasenaHash(passwordEncoder.encode(registroDTO.getPassword()));
        }
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
    public void cambiarEstadoUsuario(Integer id) {
        Usuario usuario = usuarioRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        usuario.setActivo(!usuario.isActivo());
        usuarioRepositorio.save(usuario);
    }

    @Override
    public Optional<Usuario> buscarUsuarioPorId(Integer id) {
        return usuarioRepositorio.findById(id);
    }
}