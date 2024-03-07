package by.sorface.sso.web.services.emails;

import by.sorface.sso.web.records.MailRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultEmailService implements EmailService {

    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public void send(final List<MailRequest> mails) {
        mails.forEach(it -> {
            final var message = new SimpleMailMessage();
            {
                message.setFrom(sender);
                message.setTo(it.to());
                message.setSubject(it.subject());
                message.setText(it.body());
                message.setSentDate(new Date());
            }

            emailSender.send(message);
        });
    }

}
