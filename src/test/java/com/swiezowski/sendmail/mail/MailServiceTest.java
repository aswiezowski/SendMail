package com.swiezowski.sendmail.mail;

import com.swiezowski.sendmail.mail.dto.CreateMailRequest;
import com.swiezowski.sendmail.mail.dto.ImmutableCreateMailRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

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
}