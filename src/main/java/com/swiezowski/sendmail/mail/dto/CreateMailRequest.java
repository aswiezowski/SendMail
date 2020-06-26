package com.swiezowski.sendmail.mail.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.Set;

@Value.Immutable
@JsonSerialize(as = ImmutableCreateMailRequest.class)
@JsonDeserialize(as = ImmutableCreateMailRequest.class)
public abstract class CreateMailRequest {

    public abstract String sender();
    public abstract Set<String> recipients();
    public abstract String content();

}
