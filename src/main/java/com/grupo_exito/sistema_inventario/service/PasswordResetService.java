// src/main/java/com/grupo_exito/sistema_inventario/service/PasswordResetService.java

package com.grupo_exito.sistema_inventario.service;

import com.grupo_exito.sistema_inventario.model.entity.Usuario;
import com.grupo_exito.sistema_inventario.model.repository.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void generateAndSendResetToken(String email, String siteURL) {
        Usuario usuario = usuarioRepositorio.findByCorreo(email)
                .orElse(null); // No lanzamos error para no revelar si un correo existe o no

        if (usuario != null) {
            String token = UUID.randomUUID().toString();
            usuario.setResetPasswordToken(token);
            usuario.setResetPasswordTokenExpiry(LocalDateTime.now().plusHours(1)); // Token válido por 1 hora
            usuarioRepositorio.save(usuario);

            String resetLink = siteURL + "/reset-password?token=" + token;
            sendEmail(usuario.getCorreo(), resetLink);
        }
    }

    private void sendEmail(String recipientAddress, String link) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientAddress);
        message.setSubject("Restablecimiento de Contraseña - Sistema de Inventario Éxito");
        message.setText("Hola,\n\n"
                + "Has solicitado restablecer tu contraseña.\n"
                + "Haz clic en el siguiente enlace para cambiar tu contraseña:\n\n"
                + link + "\n\n"
                + "Si no solicitaste esto, por favor ignora este correo.\n\n"
                + "Gracias,\n"
                + "Equipo de Soporte Grupo Éxito.");
        mailSender.send(message);
    }

    public Usuario validatePasswordResetToken(String token) {
        Usuario usuario = usuarioRepositorio.findByResetPasswordToken(token).orElse(null);
        if (usuario == null || usuario.getResetPasswordTokenExpiry().isBefore(LocalDateTime.now())) {
            return null; // Token no válido o expirado
        }
        return usuario;
    }

    public void updatePassword(Usuario usuario, String newPassword) {
        usuario.setContrasenaHash(passwordEncoder.encode(newPassword));
        usuario.setResetPasswordToken(null); // Invalida el token
        usuario.setResetPasswordTokenExpiry(null);
        usuarioRepositorio.save(usuario);
    }
}