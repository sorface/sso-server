package by.sorface.ssoserver.services;

import by.sorface.ssoserver.records.MailRequest;

import java.util.List;

public interface EmailService {

    void send(final List<MailRequest> mails);

}
