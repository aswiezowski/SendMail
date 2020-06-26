package com.swiezowski.sendmail.mail;

import com.swiezowski.sendmail.mail.dto.CreateMailRequest;
import com.swiezowski.sendmail.mail.dto.ImmutableCreateMailRequest;
import com.swiezowski.sendmail.mail.entities.Mail;
import com.swiezowski.sendmail.mail.entities.MailStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
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
}