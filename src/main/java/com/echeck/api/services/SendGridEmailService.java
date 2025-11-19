package com.echeck.api.services;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SendGridEmailService {

    @Value("${sendgrid.api-key}")
    private String sendGridApiKey;

    @Value("${sendgrid.from-email}")
    private String fromEmail;

    public boolean sendEmail(String toEmail, String subject, String body) {

        Email from = new Email(fromEmail);
        Email to = new Email(toEmail);
        Content content = new Content("text/html", body);

        Mail mail = new Mail(from, subject, to, content);
        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                System.out.println("E-mail enviado com sucesso via API! Status: " + response.getStatusCode());
                return true;
            } else {
                System.err.println("Falha no envio via API. Status: " + response.getStatusCode());
                System.err.println("Corpo da Resposta do SendGrid: " + response.getBody());
                return false;
            }

        } catch (IOException ex) {
            System.err.println("Erro de IO ao enviar e-mail: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }
}