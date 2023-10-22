package by.sorface.ssoserver.services;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String sender;

    private final JavaMailSender emailSender;

    public record MailRequest(String toMail, String subject, String body) {}

    public void send(final List<MailRequest> mails) {
        mails.forEach(it -> {
            final var message = new SimpleMailMessage();
            {
                message.setFrom(sender);
                message.setTo(it.toMail);
                message.setSubject(it.subject);
                message.setText(it.body);
            }

            emailSender.send(message);
        });
    }

}
