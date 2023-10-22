package by.sorface.ssoserver.services;

import java.util.List;

public interface EmailService {

    public void send(final List<EmailServiceImpl.MailRequest> mails);

}
