package com.swiezowski.sendmail.mail;

import com.swiezowski.sendmail.mail.dto.CreateMailRequest;
import com.swiezowski.sendmail.mail.entities.Mail;
import com.swiezowski.sendmail.mail.entities.MailStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MailService {

    private MailRepository mailRepository;

    @Autowired
    public MailService(MailRepository mailRepository) {
        this.mailRepository = mailRepository;
    }

    public UUID createMail(CreateMailRequest createMailRequest) {
        UUID mailUUID = UUID.randomUUID();
        Mail mail = new Mail(mailUUID,
                createMailRequest.sender(),
                createMailRequest.recipients(),
                createMailRequest.content(),
                MailStatus.PENDING);
        mailRepository.save(mail);
        return mailUUID;
    }
}
