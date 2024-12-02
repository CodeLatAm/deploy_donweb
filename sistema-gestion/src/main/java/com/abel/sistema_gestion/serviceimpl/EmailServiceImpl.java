package com.abel.sistema_gestion.serviceimpl;

import com.abel.sistema_gestion.entity.User;
import com.abel.sistema_gestion.serviceimpl.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Date;


@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String adminEmail;

    @Transactional
    @Override
    public void sendVerificationEmail(User user, String token) throws MessagingException, IOException {
        String subject = "Verificación de cuenta";
        String body = getBody(user, token);

        // Crear el mensaje MimeMessage
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        // Configurar remitente, destinatario y contenido
        helper.setFrom(this.adminEmail); // Cambia por tu email
        helper.setTo(user.getUsername());
        helper.setSubject(subject);
        helper.setText(body, true); // `true` indica que el contenido es HTML
        helper.setSentDate(new Date());
        // Obtener la imagen desde el directorio resources/static
        //File imageFile = new ClassPathResource("static/image-email.jpg").getFile();

        // Agregar la imagen como un archivo inline (embebido)
        //helper.addInline("imageCid", imageFile); // "imageCid" es el Content-ID

        // Enviar el correo
        mailSender.send(mimeMessage);

    }

    private static String getBody(User user, String token) {
        //String verificationUrl = "http://localhost:4200/auth?token=" + token;
        String verificationUrl = "https://vendepro.com.ar/auth?token=" + token;

        return "<html>" +
                "<head>" +
                "<style>" +
                "  body { font-family: Arial, sans-serif; line-height: 1.6; background-color: #f9f9f9; color: #333; padding: 20px; }" +
                "  .container { max-width: 600px; margin: 0 auto; background: #ffffff; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); padding: 20px; }" +
                "  .header { text-align: center; padding-bottom: 20px; border-bottom: 1px solid #eaeaea; }" +
                "  .header h1 { color: #ff55a5; margin: 0; }" +
                "  .content { padding: 20px 0; }" +
                "  .content h2 { color: #4CAF50; margin: 0 0 10px; }" +
                "  .content p { margin: 10px 0; }" +
                "  .button { display: inline-block; margin-top: 20px; padding: 10px 20px; background-color: #2196F3; color: #ffffff; text-decoration: none; border-radius: 5px; }" +
                "  .button:hover { background-color: #1769aa; }" +
                "  .footer { margin-top: 20px; font-size: 12px; text-align: center; color: #888; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "  <div class='container'>" +
                "    <div class='header'>" +
                "      <h1>Venta Pro</h1>" +
                "    </div>" +
                "    <div class='content'>" +
                "      <h2>¡Hola, " + user.getName() + "!</h2>" +
                "      <p>Gracias por registrarte en <strong>Venta Pro</strong>.</p>" +
                "      <p>Para completar tu registro, haz clic en el botón a continuación:</p>" +
                "      <a href='" + verificationUrl + "' class='button'>Verificar cuenta</a>" +
                "      <p>Si no solicitaste esta acción, ignora este mensaje.</p>" +
                "    </div>" +
                "    <div class='footer'>" +
                "      <p>Este es un mensaje automático. Por favor, no respondas a este correo.</p>" +
                "    </div>" +
                "  </div>" +
                "</body>" +
                "</html>";
    }
}
