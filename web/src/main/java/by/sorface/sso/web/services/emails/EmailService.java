package by.sorface.sso.web.services.emails;

import by.sorface.sso.web.records.MailRequest;

import java.util.List;

public interface EmailService {

    void send(final List<MailRequest> mails);

}
