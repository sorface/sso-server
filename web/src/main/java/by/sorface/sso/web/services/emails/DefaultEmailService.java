package by.sorface.sso.web.services.emails;

import by.sorface.sso.web.records.mails.Mail;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultEmailService implements EmailService {

    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public void send(final Mail mail) {
        final var message = new SimpleMailMessage();
        {
            message.setFrom(sender);
            message.setTo(mail.to());
            message.setSubject(mail.subject());
            message.setText(mail.body());
            message.setSentDate(new Date());
        }

        emailSender.send(message);
    }

    public void sendHtml(final Mail mail) {
        try {
            final var mimeMessage = emailSender.createMimeMessage();
            final var helper = new MimeMessageHelper(mimeMessage, true);

            helper.setFrom(sender);
            helper.setTo(mail.to());
            helper.setText(mail.body(), true);
            helper.setSentDate(new Date());

            mimeMessage.setSubject(mail.subject());

            emailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error(e.getMessage(), e);
        }
    }

}
