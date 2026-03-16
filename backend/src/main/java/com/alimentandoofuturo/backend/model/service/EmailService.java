package com.alimentandoofuturo.backend.model.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarEmail(String destinatario, String assunto, String corpo) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        
        helper.setTo(destinatario);
        helper.setSubject(assunto);
        helper.setText(corpo, true);
        
        mailSender.send(message);
    }

    public void enviarEmailRecuperacaoSenha(String destinatario, String token) throws MessagingException {
        String assunto = "Recuperação de Senha - Alimentando o Futuro";
        String corpo = String.format("""
            <html>
            <body>
                <h2>Recuperação de Senha</h2>
                <p>Você solicitou a recuperação de senha.</p>
                <p>Use o código abaixo para redefinir sua senha:</p>
                <h3>%s</h3>
                <p>Este código expira em 1 hora.</p>
            </body>
            </html>
            """, token);
        
        enviarEmail(destinatario, assunto, corpo);
    }

    public void enviarEmailSuporte(String nome, String emailUsuario, String assunto, String mensagem) throws MessagingException {
        String destinatario = "alimentandoofuturo@gmail.com";
        String assuntoEmail = "[Suporte] " + assunto;
        String corpo = String.format("""
            <html>
            <body>
                <h2>Nova Solicitação de Suporte</h2>
                <p><strong>Nome:</strong> %s</p>
                <p><strong>Email:</strong> %s</p>
                <p><strong>Assunto:</strong> %s</p>
                <p><strong>Mensagem:</strong></p>
                <p>%s</p>
            </body>
            </html>
            """, nome, emailUsuario, assunto, mensagem);
        
        enviarEmail(destinatario, assuntoEmail, corpo);
    }
}
