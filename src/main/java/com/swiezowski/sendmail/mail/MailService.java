package com.swiezowski.sendmail.mail;

import com.swiezowski.sendmail.mail.dto.CreateMailRequest;
import com.swiezowski.sendmail.mail.entities.Mail;
import com.swiezowski.sendmail.mail.entities.MailStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
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
                new ArrayList<>(createMailRequest.recipients()),
                createMailRequest.content(),
                MailStatus.PENDING);
        mailRepository.save(mail);
        return mailUUID;
    }

    public MailStatus getStatus(UUID mailUUID){
        return mailRepository
                .findByUuid(mailUUID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
                .getStatus();
    }

    public Mail get(UUID mailUUID) {
        return mailRepository
                .findByUuid(mailUUID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
