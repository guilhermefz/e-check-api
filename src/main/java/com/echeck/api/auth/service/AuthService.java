package com.echeck.api.auth.service;

import com.echeck.api.auth.dto.ForgotPasswordRequestDto;
import com.echeck.api.auth.dto.ResetPasswordRequestDto;
import com.echeck.api.model.Usuario;
import com.echeck.api.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final EmailService emailService;
    private final UsuarioRepository usuarioRepository; // Injeção do repositório
    private final PasswordEncoder passwordEncoder;     // Injeção do PasswordEncoder

    // URL base do seu Front-end (MUDAR ESTE VALOR)
    private static final String FRONTEND_URL = "http://localhost:3000/redefinir-senha";

    // FLUXO 1: Esqueceu a Senha
    public void forgotPassword(ForgotPasswordRequestDto forgotPasswordRequestDto) {
        String email = forgotPasswordRequestDto.getEmail();

        Optional<Usuario> userOptional = usuarioRepository.findByEmail(email);

        // Ação só é executada se o usuário existir (boa prática de segurança)
        if (userOptional.isPresent()) {
            Usuario usuario = userOptional.get();

            // 1. Gerar e salvar o token
            String token = UUID.randomUUID().toString();
            Instant expires = Instant.now().plus(1, ChronoUnit.HOURS); // Expira em 1 hora

            usuario.setResetPasswordToken(token);
            usuario.setResetPasswordExpires(expires);
            usuarioRepository.save(usuario);

            // 2. Montar o link
            String resetLink = FRONTEND_URL + "?token=" + token;

            // 3. Enviar o e-mail
            emailService.sendPasswordResetLink(email, resetLink);
        }

        // Retorna sucesso em ambos os casos para evitar enumerar usuários.
    }

    // FLUXO 2: Redefinir a Senha com o Token
    public void resetPassword(ResetPasswordRequestDto dto) {
        // 1. Buscar usuário pelo token
        Usuario usuario = usuarioRepository.findByResetPasswordToken(dto.getToken())
                .orElseThrow(() -> new RuntimeException("Token inválido ou expirado."));

        // 2. Verificar a validade
        if (usuario.getResetPasswordExpires() == null || usuario.getResetPasswordExpires().isBefore(Instant.now())) {
            throw new RuntimeException("Token expirado. Solicite uma nova redefinição.");
        }

        // 3. Atualizar a senha (hash)
        usuario.setSenha(passwordEncoder.encode(dto.getNewPassword()));

        // 4. Invalidar o token
        usuario.setResetPasswordToken(null);
        usuario.setResetPasswordExpires(null);
        usuarioRepository.save(usuario);
    }
}