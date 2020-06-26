package com.swiezowski.sendmail.mail;

import com.swiezowski.sendmail.mail.dto.*;
import com.swiezowski.sendmail.mail.entities.Mail;
import com.swiezowski.sendmail.mail.entities.MailStatus;
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

    @GetMapping(path = "{mailUUID}/status")
    public GetStatusResponse getStatus(@PathVariable("mailUUID") UUID mailUUID){
        MailStatus status = mailService.getStatus(mailUUID);

        return ImmutableGetStatusResponse.builder()
                .status(mapMailStatusToDto(status))
                .build();
    }

    @GetMapping(path = "{mailUUID}")
    public GetMailResponse get(@PathVariable("mailUUID") UUID mailUUID){
        Mail mail = mailService.get(mailUUID);

        return ImmutableGetMailResponse.builder()
                .sender(mail.getSender())
                .recipients(mail.getRecipients())
                .content(mail.getContent())
                .status(mapMailStatusToDto(mail.getStatus()))
                .build();
    }

    private MailStatusDto mapMailStatusToDto(MailStatus status) {
        return MailStatusDto.valueOf(status.name());
    }

}
