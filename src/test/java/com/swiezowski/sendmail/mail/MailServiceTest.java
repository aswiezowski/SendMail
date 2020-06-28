package com.swiezowski.sendmail.mail;

import com.swiezowski.sendmail.mail.dto.CreateMailRequest;
import com.swiezowski.sendmail.mail.dto.ImmutableCreateMailRequest;
import com.swiezowski.sendmail.mail.dto.MailStatusDto;
import com.swiezowski.sendmail.mail.dto.PatchMailRequest;
import com.swiezowski.sendmail.mail.entities.Mail;
import com.swiezowski.sendmail.mail.entities.MailStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MailServiceTest {

    @Test
    void createMailReturnsUUID() {
        MailRepository repository = Mockito.mock(MailRepository.class);
        SendEmailService sendEmailService = Mockito.mock(SendEmailService.class);
        MailService service = new MailService(repository, sendEmailService);
        CreateMailRequest createMailRequest = ImmutableCreateMailRequest.builder()
                .sender("me@example.com")
                .content("content")
                .subject("test subject")
                .build();

        UUID mailUUID = service.createMail(createMailRequest);

        assertNotNull(mailUUID);
    }

    @Test
    void getStatusThrowsExceptionWhenUUIDNotFound() {
        MailRepository repository = Mockito.mock(MailRepository.class);
        SendEmailService sendEmailService = Mockito.mock(SendEmailService.class);
        MailService service = new MailService(repository, sendEmailService);
        UUID notExistentUUID = UUID.fromString("2d807865-f774-454d-b726-70eaa158b984");

        assertThrows(ResponseStatusException.class, () -> service.getStatus(notExistentUUID));
    }

    @Test
    void getStatusReturnsStatusWhenMailFound() {
        MailRepository repository = Mockito.mock(MailRepository.class);
        UUID mailUUID = UUID.fromString("cd69ff1a-7171-4d25-b7d4-65a981b1603f");
        Mail mail = mock(Mail.class);
        when(mail.getStatus()).thenReturn(MailStatus.SENT);
        when(repository.findByUuid(mailUUID)).thenReturn(Optional.of(mail));
        SendEmailService sendEmailService = Mockito.mock(SendEmailService.class);
        MailService service = new MailService(repository, sendEmailService);

        MailStatus status = service.getStatus(mailUUID);

        assertEquals(MailStatus.SENT, status);
    }

    @Test
    void getMailThrowsExceptionWhenUUIDNotFound() {
        MailRepository repository = Mockito.mock(MailRepository.class);
        SendEmailService sendEmailService = Mockito.mock(SendEmailService.class);
        MailService service = new MailService(repository, sendEmailService);
        UUID notExistentUUID = UUID.fromString("9bb92b49-c21c-4682-8a8c-d4ed7081ffac");

        assertThrows(ResponseStatusException.class, () -> service.get(notExistentUUID));
    }

    @Test
    void getMailReturnsMail() {
        MailRepository repository = Mockito.mock(MailRepository.class);
        SendEmailService sendEmailService = Mockito.mock(SendEmailService.class);
        MailService service = new MailService(repository, sendEmailService);
        Mail expectedMail = mock(Mail.class);
        UUID mailUUID = UUID.fromString("9bb92b49-c21c-4682-8a8c-d4ed7081ffac");
        when(repository.findByUuid(mailUUID)).thenReturn(Optional.of(expectedMail));


        Mail mail = service.get(mailUUID);

        assertEquals(expectedMail, mail);
    }

    @Test
    void getAllMailsReturnsPage() {
        MailRepository repository = Mockito.mock(MailRepository.class);
        SendEmailService sendEmailService = Mockito.mock(SendEmailService.class);
        MailService service = new MailService(repository, sendEmailService);
        Page page = mock(Page.class);
        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Mail> pagedMails = service.getAll(Pageable.unpaged());

        assertNotNull(pagedMails);
    }

    @Test
    void patchMailThrowsExceptionWhenQueryStatusNotEqualsPending() {
        MailRepository repository = Mockito.mock(MailRepository.class);
        SendEmailService sendEmailService = Mockito.mock(SendEmailService.class);
        MailService service = new MailService(repository, sendEmailService);
        PatchMailRequest mailRequest = mock(PatchMailRequest.class);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> service.patchMail(MailStatusDto.SENT, mailRequest));
        assertEquals("Only pending status query param is supported", exception.getReason());
    }

    @Test
    void patchMailThrowsExceptionWhenBodyStatusNotEqualsSent() {
        MailRepository repository = Mockito.mock(MailRepository.class);
        SendEmailService sendEmailService = Mockito.mock(SendEmailService.class);
        MailService service = new MailService(repository, sendEmailService);
        PatchMailRequest mailRequest = mock(PatchMailRequest.class);
        when(mailRequest.status()).thenReturn(MailStatusDto.PENDING);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> service.patchMail(MailStatusDto.PENDING, mailRequest));
        assertEquals("Only sent status in body is supported", exception.getReason());
    }

    @Test
    void patchMailSendsMessageWhenAnyIsPending() {
        MailRepository repository = Mockito.mock(MailRepository.class);
        Mail mail = mock(Mail.class);
        UUID mailUUID = UUID.fromString("b63bba82-6eac-4432-a69c-140fa16f80a3");
        when(mail.getUuid()).thenReturn(mailUUID);
        when(repository.findByStatus(MailStatus.PENDING)).thenReturn(Collections.singletonList(mail));
        SendEmailService sendEmailService = Mockito.mock(SendEmailService.class);
        MailService service = new MailService(repository, sendEmailService);
        PatchMailRequest mailRequest = mock(PatchMailRequest.class);
        when(mailRequest.status()).thenReturn(MailStatusDto.SENT);

        Set<UUID> sentMailUUIDs = service.patchMail(MailStatusDto.PENDING, mailRequest);

        assertIterableEquals(Collections.singletonList(mailUUID), sentMailUUIDs);
        verify(sendEmailService).sendMails(anyCollection());
    }
}