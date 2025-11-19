package com.echeck.api.auth.service;

import com.echeck.api.auth.dto.ForgotPasswordRequestDto;
import com.echeck.api.auth.dto.ResetPasswordRequestDto;
import com.echeck.api.model.Usuario;
import com.echeck.api.repositories.UsuarioRepository;
import com.echeck.api.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder; // NOVO: Import para criptografia
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    // INJEÇÕES DE DEPENDÊNCIA
    private final UsuarioService usuarioService;
    private final EmailService emailService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder; // NOVO: Injeção para criptografar senhas

    /**
     * Processa a requisição de recuperação de senha: gera o token, salva no DB e envia e-mail.
     * @param dto Contém o e-mail do usuário.
     */
    public void forgotPassword(ForgotPasswordRequestDto dto) {

        Usuario usuario = usuarioService.buscarPorEmail(dto.getEmail())
                .orElse(null);

        if (usuario == null) {
            System.out.println("Tentativa de recuperação de senha para e-mail não registrado: " + dto.getEmail());
            return;
        }

        // 3. Geração e Persistência do Token
        String token = UUID.randomUUID().toString();
        Instant expirationTime = Instant.now().plus(24, ChronoUnit.HOURS);

        usuario.setResetToken(token);
        usuario.setTokenExpiration(expirationTime);

        usuarioRepository.save(usuario);

        // 4. Envia o e-mail com o token
        String assunto = "Recuperação de Senha E-Check API";
        String corpoEmail = "<h1>Recuperação de Senha</h1><p>Olá " + usuario.getNome() +
                ",</p><p>Seu código de recuperação é: <strong>" + token +
                "</strong></p><p>Este código expira em 24 horas. Use-o para definir uma nova senha.</p>";

        emailService.sendEmail(usuario.getEmail(), assunto, corpoEmail);
    }

    /**
     * Lógica para redefinir a senha usando o token.
     * @param dto Contém o token e a nova senha.
     */
    public void resetPassword(ResetPasswordRequestDto dto) {

        // 1. Buscar usuário pelo token
        Usuario usuario = usuarioRepository.findByResetToken(dto.getToken())
                .orElseThrow(() -> new RuntimeException("Token de redefinição inválido ou não encontrado."));

        // 2. Verificar se o token expirou
        if (usuario.getTokenExpiration().isBefore(Instant.now())) {
            // Limpar token expirado para que não possa ser tentado novamente
            usuario.setResetToken(null);
            usuario.setTokenExpiration(null);
            usuarioRepository.save(usuario);
            throw new RuntimeException("Token de redefinição expirado. Por favor, solicite uma nova recuperação.");
        }

        // 3. Criptografar e definir a nova senha
        String encryptedPassword = passwordEncoder.encode(dto.getNewPassword());
        usuario.setSenha(encryptedPassword);

        // 4. Limpar os campos do token para invalidar o uso futuro (token de uso único)
        usuario.setResetToken(null);
        usuario.setTokenExpiration(null);

        // 5. Salvar o objeto atualizado no banco
        usuarioRepository.save(usuario);
    }
}