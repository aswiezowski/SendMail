package com.swiezowski.sendmail.mail;

import com.swiezowski.sendmail.mail.dto.CreateMailRequest;
import com.swiezowski.sendmail.mail.dto.ImmutableCreateMailRequest;
import com.swiezowski.sendmail.mail.entities.Mail;
import com.swiezowski.sendmail.mail.entities.MailStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MailServiceTest {

    @Test
    void createMailReturnsUUID() {
        MailRepository repository = Mockito.mock(MailRepository.class);
        MailService service = new MailService(repository);
        CreateMailRequest createMailRequest = ImmutableCreateMailRequest.builder()
                .sender("me@example.com")
                .content("content")
                .build();

        UUID mailUUID = service.createMail(createMailRequest);

        assertNotNull(mailUUID);
    }

    @Test
    void getStatusThrowsExceptionWhenUUIDNotFound(){
        MailRepository repository = Mockito.mock(MailRepository.class);
        MailService service = new MailService(repository);
        UUID notExistentUUID = UUID.fromString("2d807865-f774-454d-b726-70eaa158b984");

        assertThrows(ResponseStatusException.class, () -> service.getStatus(notExistentUUID));
    }

    @Test
    void getStatusReturnsStatusWhenMailFound(){
        MailRepository repository = Mockito.mock(MailRepository.class);
        UUID mailUUID = UUID.fromString("cd69ff1a-7171-4d25-b7d4-65a981b1603f");
        Mail mail = mock(Mail.class);
        when(mail.getStatus()).thenReturn(MailStatus.SENT);
        when(repository.findByUuid(mailUUID)).thenReturn(Optional.of(mail));
        MailService service = new MailService(repository);

        MailStatus status = service.getStatus(mailUUID);

        assertEquals(MailStatus.SENT, status);
    }

    @Test
    void getMailThrowsExceptionWhenUUIDNotFound(){
        MailRepository repository = Mockito.mock(MailRepository.class);
        MailService service = new MailService(repository);
        UUID notExistentUUID = UUID.fromString("9bb92b49-c21c-4682-8a8c-d4ed7081ffac");

        assertThrows(ResponseStatusException.class, () -> service.get(notExistentUUID));
    }

    @Test
    void getMailReturnsMail(){
        MailRepository repository = Mockito.mock(MailRepository.class);
        MailService service = new MailService(repository);
        Mail expectedMail = mock(Mail.class);
        UUID mailUUID = UUID.fromString("9bb92b49-c21c-4682-8a8c-d4ed7081ffac");
        when(repository.findByUuid(mailUUID)).thenReturn(Optional.of(expectedMail));


        Mail mail = service.get(mailUUID);

        assertEquals(expectedMail, mail);
    }

    @Test
    void getAllMailsReturnsPage(){
        MailRepository repository = Mockito.mock(MailRepository.class);
        MailService service = new MailService(repository);
        Page page = mock(Page.class);
        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Mail> pagedMails = service.getAll(Pageable.unpaged());

        assertNotNull(pagedMails);
    }
}