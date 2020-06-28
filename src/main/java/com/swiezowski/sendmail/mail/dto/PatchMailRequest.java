package com.swiezowski.sendmail.mail.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutablePatchMailRequest.class)
@JsonDeserialize(as = ImmutablePatchMailRequest.class)
public abstract class PatchMailRequest {

    public abstract MailStatusDto status();
}
