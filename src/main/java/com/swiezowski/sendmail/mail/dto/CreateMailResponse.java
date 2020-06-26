package com.swiezowski.sendmail.mail.dto;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.UUID;

@Value.Immutable
@JsonSerialize(as = ImmutableCreateMailResponse.class)
@JsonDeserialize(as = ImmutableCreateMailResponse.class)
public abstract class CreateMailResponse {

    public abstract UUID uuid();
}
