package com.swiezowski.sendmail.mail;

import com.swiezowski.sendmail.mail.dto.CreateMailRequest;
import com.swiezowski.sendmail.mail.dto.CreateMailResponse;
import com.swiezowski.sendmail.mail.dto.ImmutableCreateMailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("mail")
public class MailResource {

    private MailService mailService;

    @Autowired
    public MailResource(MailService mailService){
        this.mailService = mailService;
    }

    @PostMapping
    public CreateMailResponse create(@RequestBody CreateMailRequest mailRequest){
        UUID mailUUID = mailService.createMail(mailRequest);
        return ImmutableCreateMailResponse.builder()
                .uuid(mailUUID)
                .build();
    }

}
