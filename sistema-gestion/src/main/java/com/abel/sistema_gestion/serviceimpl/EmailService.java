package com.abel.sistema_gestion.serviceimpl;


import jakarta.activation.DataHandler;
import jakarta.mail.Message;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource; // Usar la clase correcta
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;

import jakarta.mail.internet.MimeMultipart;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.activation.DataSource;


import java.util.Base64;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    @Value("spring.mail.username")
    private String adminEmail;

    public void sendEmailWithPDF(String email, String pdfBase64) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeBodyPart bodyPart = new MimeBodyPart();
            MimeBodyPart attachmentPart = new MimeBodyPart();

            bodyPart.setText("Por favor, encuentra tu factura en PDF.");

            // Decodificar el PDF desde Base64
            byte[] pdfBytes = Base64.getDecoder().decode(pdfBase64);
            DataSource dataSource = (DataSource) new ByteArrayDataSource(pdfBytes, "application/pdf");
            attachmentPart.setDataHandler(new DataHandler((jakarta.activation.DataSource) dataSource));
            attachmentPart.setFileName("factura.pdf");

            MimeMultipart multipart = new MimeMultipart();
            multipart.addBodyPart(bodyPart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);
            message.setSubject("Tu factura Adjunto");
            message.setFrom(new InternetAddress(this.adminEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));

            mailSender.send(message);
            System.out.println("Correo enviado a: " + email);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
