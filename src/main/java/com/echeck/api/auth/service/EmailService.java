package com.echeck.api.auth.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendPasswordResetLink(String email, String resetLink) {
        try {
            // Cria uma nova mensagem de e-mail no formato Mime
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            // Cria um helper para facilitar a montagem do e-mail
            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage, true, "UTF-8");

            // O e-mail do remetente deve ser configurado no application.properties
            helper.setFrom("seu-email@dominio.com");
            helper.setTo(email);
            helper.setSubject("Redefinição de Senha - E-Check API");

            // Montar o corpo HTML dinamicamente com o link
            String htmlBody = "<!DOCTYPE html>"
                    + "<html><body style='font-family: Arial;'>"
                    + "<div style='background-color: #FFF; padding:20px; border-radius:8px; text-align: center;'>"
                    + "<h2>Solicitação de Redefinição de Senha</h2>"
                    + "<p>Você solicitou a redefinição de senha para sua conta.</p>"
                    + "<p>Clique no link/botão abaixo para criar uma nova senha. Este link expira em 1 hora.</p>"
                    + "<a href=\"" + resetLink + "\" style='display: inline-block; padding: 10px 20px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px; margin-top: 15px;'>"
                    + "Redefinir Minha Senha"
                    + "</a>"
                    + "<p style='margin-top: 20px; font-size: small; color: #888;'>Se você não solicitou esta alteração, ignore este e-mail.</p>"
                    + "</div></body></html>";

            helper.setText(htmlBody, true);
            mailSender.send(mimeMessage);
        }catch (Exception e){
            throw new RuntimeException("Falha ao enviar email: "+e.getMessage());
        }
    }
}