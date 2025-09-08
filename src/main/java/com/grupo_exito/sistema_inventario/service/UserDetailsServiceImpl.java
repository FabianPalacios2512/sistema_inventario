package com.grupo_exito.sistema_inventario.service; // <-- PAQUETE ACTUALIZADO

import com.grupo_exito.sistema_inventario.model.entity.Usuario;
import com.grupo_exito.sistema_inventario.model.repository.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         
        Usuario usuario = usuarioRepositorio.findByCorreo(username) // se toma el correo y se le pide a la base de datos que lo busque
                .orElseThrow(() -> new UsernameNotFoundException("El usuario con email " + username + " no existe."));

        Collection<? extends GrantedAuthority> authorities = usuario.getRoles()
                .stream()
                .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getNombre()))
                .collect(Collectors.toList());
        return new User(
                        usuario.getNombre(), // <-- Usar el correo como username
                        usuario.getContrasenaHash(),
                        usuario.isActivo(),
                        true,
                        true,
                        true,
                        authorities);
    }
}
