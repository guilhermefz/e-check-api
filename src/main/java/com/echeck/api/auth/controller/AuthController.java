package com.echeck.api.auth.controller;

import com.echeck.api.auth.dto.ForgotPasswordRequestDto;
import com.echeck.api.auth.dto.ResetPasswordRequestDto;
import com.echeck.api.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // Rota 1: Enviar link de recuperação
    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword
    (@RequestBody @Valid ForgotPasswordRequestDto forgotPasswordRequestDto) {
        try {
            authService.forgotPassword(forgotPasswordRequestDto);
            // Mensagem genérica por segurança.
            return ResponseEntity.ok("Se o e-mail estiver registrado, as instruções foram enviadas.");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body("Erro interno ao processar a solicitação: "+ e.getMessage());
        }
    }

    // Rota 2: Redefinir a senha com o token
    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword
    (@RequestBody @Valid ResetPasswordRequestDto resetPasswordRequestDto) {
        try {
            authService.resetPassword(resetPasswordRequestDto);
            return ResponseEntity.ok("Senha redefinida com sucesso!");
        }catch (RuntimeException e) {
            // Trata erros de token inválido/expirado
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body("Erro: "+ e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body("Erro interno ao redefinir a senha: "+ e.getMessage());
        }
    }
}