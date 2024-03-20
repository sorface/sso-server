package by.sorface.sso.web.services.emails;

import by.sorface.sso.web.records.mails.Mail;
import by.sorface.sso.web.records.mails.MailTemplate;

public interface EmailService {

    void send(final Mail mail);

    void sendHtml(final MailTemplate mailTemplate);

}
