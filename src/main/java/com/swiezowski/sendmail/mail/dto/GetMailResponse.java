package com.swiezowski.sendmail.mail.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.Set;

@Value.Immutable
@JsonSerialize(as = ImmutableGetMailResponse.class)
@JsonDeserialize(as = ImmutableGetMailResponse.class)
public abstract class GetMailResponse {

    public abstract String sender();
    public abstract Set<String> recipients();
    public abstract String content();
    public abstract MailStatusDto status();

}

