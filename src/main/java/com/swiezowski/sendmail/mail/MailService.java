package com.swiezowski.sendmail.mail;

import com.swiezowski.sendmail.mail.dto.CreateMailRequest;
import com.swiezowski.sendmail.mail.dto.MailStatusDto;
import com.swiezowski.sendmail.mail.dto.PatchMailRequest;
import com.swiezowski.sendmail.mail.entities.Mail;
import com.swiezowski.sendmail.mail.entities.MailStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class MailService {

    private MailRepository mailRepository;
    private SendEmailService sendEmailService;

    @Autowired
    public MailService(MailRepository mailRepository, SendEmailService sendEmailService) {
        this.mailRepository = mailRepository;
        this.sendEmailService = sendEmailService;
    }

    public UUID createMail(CreateMailRequest createMailRequest) {
        UUID mailUUID = UUID.randomUUID();
        Mail mail = new Mail(mailUUID,
                createMailRequest.sender(),
                new ArrayList<>(createMailRequest.recipients()),
                createMailRequest.content(),
                createMailRequest.subject(),
                MailStatus.PENDING);
        mailRepository.save(mail);
        return mailUUID;
    }

    public MailStatus getStatus(UUID mailUUID) {
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

    public Page<Mail> getAll(Pageable pageable) {
        return mailRepository.findAll(pageable);
    }

    public Set<UUID> patchMail(MailStatusDto status, PatchMailRequest patchMailRequest) {
        if (!MailStatusDto.PENDING.equals(status)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only " + MailStatusDto.PENDING +
                    " status query param is supported");
        }
        if (!MailStatusDto.SENT.equals(patchMailRequest.status())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only " + MailStatusDto.SENT +
                    " status in body is supported");
        }

        List<Mail> mailsToSend = mailRepository.findByStatus(MailStatus.PENDING);
        sendEmailService.sendMails(mailsToSend);
        mailsToSend.stream().forEach(mail -> mail.setStatus(MailStatus.SENT));
        mailRepository.saveAll(mailsToSend);

        return mailsToSend.stream()
                .map(Mail::getUuid)
                .collect(Collectors.toSet());
    }
}
