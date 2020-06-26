package com.swiezowski.sendmail.mail.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableGetStatusResponse.class)
@JsonDeserialize(as = ImmutableGetStatusResponse.class)
public abstract class GetStatusResponse {

    public abstract MailStatusDto status();

}
