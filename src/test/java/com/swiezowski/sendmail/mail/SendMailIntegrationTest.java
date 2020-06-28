package com.swiezowski.sendmail.mail;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.swiezowski.sendmail.mail.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.ActiveProfiles;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class SendMailIntegrationTest {

    public static final UUID PREDEFINED_MAIL_UUID = UUID.fromString("1a202ff4-0131-431e-8342-a6c3abec6ec1");
    public static final String PREDEFINED_MAIL_FROM = "me@example.com";
    public static final String PREDEFINED_MAIL_CONTENT = "content";
    public static final String PREDEFINED_MAIL_RECIPIENT_1 = "recipient1@company.com";
    public static final String PREDEFINED_MAIL_RECIPIENT_2 = "recipient2@company.com";
    public static final String PREDEFINED_MAIL_SUBJECT = "predefined mail subject";

    @Test
    void createTestReturnsUUID(@Autowired TestRestTemplate restTemplate) {
        ImmutableCreateMailRequest mailRequest = ImmutableCreateMailRequest.builder()
                .content("test content")
                .sender("sender@sender.com")
                .subject("test subject")
                .addRecipients("recipent@server.com")
                .build();

        ResponseEntity<CreateMailResponse> response = restTemplate.postForEntity("/mails", mailRequest, CreateMailResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().uuid());
    }

    @Test
    void getMailReturnsCorrectMail(@Autowired TestRestTemplate restTemplate) {
        ResponseEntity<GetMailResponse> response = restTemplate.getForEntity("/mails/1a202ff4-0131-431e-8342-a6c3abec6ec1", GetMailResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        GetMailResponse mail = response.getBody();
        assertEquals(PREDEFINED_MAIL_UUID, mail.uuid());
        assertEquals(PREDEFINED_MAIL_CONTENT, mail.content());
        assertEquals(PREDEFINED_MAIL_FROM, mail.sender());
        assertEquals(PREDEFINED_MAIL_SUBJECT, mail.subject());
        assertEquals(MailStatusDto.PENDING, mail.status());
        assertTrue(mail.recipients().contains(PREDEFINED_MAIL_RECIPIENT_1));
        assertTrue(mail.recipients().contains(PREDEFINED_MAIL_RECIPIENT_2));
    }

    @Test
    void getAllMailsReturnsMails(@Autowired TestRestTemplate restTemplate) {
        ResponseEntity<PagedModel> response = restTemplate.getForEntity("/mails",
                PagedModel.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        PagedModel pagedModel = response.getBody();
        assertTrue(pagedModel.getMetadata().getTotalElements() >= 1);
    }


    @Test
    void patchMailsReturnsMailsUUIDS(@Autowired TestRestTemplate restTemplate) throws MessagingException, IOException {
        GreenMail greenMail = new GreenMail(ServerSetupTest.SMTP);
        greenMail.start();
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        PatchMailRequest patchMailRequest = ImmutablePatchMailRequest.builder().status(MailStatusDto.SENT).build();

        PatchMailResponse response = restTemplate.patchForObject("/mails?status=pending",
                patchMailRequest, PatchMailResponse.class);

        assertTrue(response.uuids().contains(PREDEFINED_MAIL_UUID));
        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertTrue(receivedMessages.length > 0);
        Optional<MimeMessage> mail = Arrays.stream(receivedMessages)
                .filter(filteredMail -> getSubject(filteredMail).equals("predefined mail subject"))
                .findFirst();
        assertTrue(mail.isPresent());
        assertEquals(PREDEFINED_MAIL_FROM, mail.get().getFrom()[0].toString());
        assertEquals(PREDEFINED_MAIL_CONTENT, mail.get().getContent().toString().trim());
        assertEquals(PREDEFINED_MAIL_SUBJECT, mail.get().getSubject().trim());
        Set<String> recipients = Arrays.stream(mail.get().getAllRecipients()).map(Address::toString).collect(Collectors.toSet());
        assertTrue(recipients.contains(PREDEFINED_MAIL_RECIPIENT_1));
        assertTrue(recipients.contains(PREDEFINED_MAIL_RECIPIENT_2));

        greenMail.stop();
    }

    private String getSubject(MimeMessage filteredMail) {
        try {
            return filteredMail.getSubject();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
