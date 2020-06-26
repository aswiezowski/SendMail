package com.swiezowski.sendmail.mail;

import com.sun.istack.NotNull;
import com.swiezowski.sendmail.mail.dto.*;
import com.swiezowski.sendmail.mail.entities.Mail;
import com.swiezowski.sendmail.mail.entities.MailStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("mail")
public class MailResource {

    private MailService mailService;
    private PagedResourcesAssembler<GetMailResponse> pagedResourcesAssembler;

    @Autowired
    public MailResource(MailService mailService, PagedResourcesAssembler<GetMailResponse> pagedResourcesAssembler){
        this.mailService = mailService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
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

        return mapToGetMailResponse(mail);
    }

    @GetMapping
    public PagedModel<EntityModel<GetMailResponse>> getAll(@NotNull Pageable pageable){
        Page<Mail> mailEntities = mailService.getAll(pageable);
        Page<GetMailResponse> mailsResponse = mailEntities.map(this::mapToGetMailResponse);
        return pagedResourcesAssembler.toModel(mailsResponse);
    }

    private ImmutableGetMailResponse mapToGetMailResponse(Mail mail) {
        return ImmutableGetMailResponse.builder()
                .uuid(mail.getUuid())
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
