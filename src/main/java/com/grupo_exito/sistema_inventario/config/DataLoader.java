package com.grupo_exito.sistema_inventario.config;

import com.grupo_exito.sistema_inventario.model.entity.Rol;
import com.grupo_exito.sistema_inventario.model.entity.Usuario;
import com.grupo_exito.sistema_inventario.model.repository.RolRepository;
import com.grupo_exito.sistema_inventario.model.repository.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Buscar o crear roles
        Optional<Rol> adminRole = rolRepository.findByNombre("ADMINISTRADOR");
        if (adminRole.isEmpty()) {
            Rol newRol = new Rol();
            newRol.setNombre("ADMINISTRADOR");
            rolRepository.save(newRol);
        }

        Optional<Rol> gerenteRole = rolRepository.findByNombre("CONTROLADOR_INVENTARIO");
        if (gerenteRole.isEmpty()) {
            Rol newRol = new Rol();
            newRol.setNombre("CONTROLADOR_INVENTARIO");
            rolRepository.save(newRol);
        }

        // Crear usuario Administrador
        if (usuarioRepositorio.findByCorreo("yoiser.agualimpia.4935@miremington.edu.co").isEmpty()) {
            Rol rolAdmin = rolRepository.findByNombre("ADMINISTRADOR").orElseThrow();
            
            Usuario admin = new Usuario();
            admin.setNombre("YOISE");
            admin.setApellido("PALACIOSs");
            admin.setCorreo("yoiser.agualimpia.4935@miremington.edu.co");
            admin.setContrasenaHash(passwordEncoder.encode("12345"));
            admin.setActivo(true);
            admin.setFechaCreacion(LocalDateTime.now());
            admin.setRoles(Set.of(rolAdmin));
            usuarioRepositorio.save(admin);
        }

         if (usuarioRepositorio.findByCorreo("fpaternina12@gmail.com").isEmpty()) {
            Rol rolAdmin = rolRepository.findByNombre("ADMINISTRADOR").orElseThrow();
            
            Usuario admin = new Usuario();
            admin.setNombre("fabian");
            admin.setApellido("PALACIOSs");
            admin.setCorreo("fpaternina12@gmail.com");
            admin.setContrasenaHash(passwordEncoder.encode("12345"));
            admin.setActivo(true);
            admin.setFechaCreacion(LocalDateTime.now());
            admin.setRoles(Set.of(rolAdmin));
            usuarioRepositorio.save(admin);
        }

        // Crear usuario Gerente de Inventario
        if (usuarioRepositorio.findByCorreo("gerente.inventario@grupoexito.com").isEmpty()) {
            Rol rolGerente = rolRepository.findByNombre("CONTROLADOR_INVENTARIO").orElseThrow();
            
            Usuario gerente = new Usuario();
            gerente.setNombre("Fabian");
            gerente.setApellido("Paternina");
            gerente.setCorreo("gerente.inventario@grupoexito.com");
            gerente.setContrasenaHash(passwordEncoder.encode("gerente123"));
            gerente.setActivo(true);
            gerente.setFechaCreacion(LocalDateTime.now());
            gerente.setRoles(Set.of(rolGerente));
            usuarioRepositorio.save(gerente);
        }
    }
}
