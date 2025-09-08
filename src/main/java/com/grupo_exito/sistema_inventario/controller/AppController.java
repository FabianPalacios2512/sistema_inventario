package com.grupo_exito.sistema_inventario.controller;

import com.grupo_exito.sistema_inventario.model.entity.Usuario;
import com.grupo_exito.sistema_inventario.service.PasswordResetService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AppController {

    @Autowired
    private PasswordResetService passwordResetService;

    @GetMapping("/login")
    public String mostrarPaginaLogin() {
        return "login";
    }

    @GetMapping("/")
    public String mostrarPaginaInicio() {
        return "index";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot_password_form";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        String siteURL = request.getRequestURL().toString().replace(request.getServletPath(), "");
        
        try {
            passwordResetService.generateAndSendResetToken(email, siteURL);
            model.addAttribute("message", "Si tu correo está registrado, hemos enviado un enlace para restablecer tu contraseña.");
        } catch (Exception e) {
            // No mostramos un error específico para no revelar información
            model.addAttribute("message", "Si tu correo está registrado, hemos enviado un enlace para restablecer tu contraseña.");
        }
        
        return "forgot_password_form";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam String token, Model model) {
        Usuario usuario = passwordResetService.validatePasswordResetToken(token);
        if (usuario == null) {
            model.addAttribute("error", "El enlace para restablecer la contraseña es inválido o ha expirado.");
            return "message_page";
        }
        model.addAttribute("token", token);
        return "reset_password_form";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Las contraseñas no coinciden. Inténtalo de nuevo.");
            model.addAttribute("token", token);
            return "reset_password_form";
        }
        
        Usuario usuario = passwordResetService.validatePasswordResetToken(token);
        if (usuario == null) {
            model.addAttribute("error", "El enlace para restablecer la contraseña es inválido o ha expirado.");
            return "message_page";
        }
        
        passwordResetService.updatePassword(usuario, password);
        
        redirectAttributes.addFlashAttribute("success", "Tu contraseña ha sido cambiada exitosamente. Ya puedes iniciar sesión.");
        return "redirect:/login";
    }
}