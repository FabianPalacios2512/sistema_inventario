package com.grupo_exito.sistema_inventario.controller;

import com.grupo_exito.sistema_inventario.dto.UsuarioRegistroDTO;
import com.grupo_exito.sistema_inventario.model.entity.Rol;
import com.grupo_exito.sistema_inventario.model.repository.RolRepository;
import com.grupo_exito.sistema_inventario.service.AdminUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/usuarios")
public class AdminUsuarioController {

    @Autowired
    private AdminUsuarioService adminUsuarioService;

    @Autowired
    private RolRepository rolRepository;

    @GetMapping
    public String mostrarPaginaAdminUsuarios(Model model) {
        model.addAttribute("usuarios", adminUsuarioService.listarTodosLosUsuarios());
        model.addAttribute("todosLosRoles", rolRepository.findAll());
        if (!model.containsAttribute("usuarioDto")) {
            model.addAttribute("usuarioDto", new UsuarioRegistroDTO());
        }
        return "admin-usuarios";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute("usuarioDto") UsuarioRegistroDTO registroDTO, RedirectAttributes redirectAttributes) {
        try {
            adminUsuarioService.guardarUsuario(registroDTO);
            redirectAttributes.addFlashAttribute("success", "Usuario guardado exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el usuario: " + e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/cambiar-estado/{id}")
    public String cambiarEstadoUsuario(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            adminUsuarioService.cambiarEstadoUsuario(id);
            redirectAttributes.addFlashAttribute("success", "Estado del usuario cambiado exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cambiar el estado del usuario.");
        }
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public UsuarioRegistroDTO obtenerUsuarioParaEditar(@PathVariable Integer id) {
        return adminUsuarioService.buscarUsuarioPorId(id)
                .map(usuario -> {
                    UsuarioRegistroDTO dto = new UsuarioRegistroDTO();
                    dto.setId(usuario.getId());
                    dto.setNombre(usuario.getNombre());
                    dto.setApellido(usuario.getApellido());
                    dto.setCorreo(usuario.getCorreo());
                    dto.setRolesIds(usuario.getRoles().stream().map(Rol::getId).collect(Collectors.toList()));
                    return dto;
                })
                .orElse(null);
    }
}