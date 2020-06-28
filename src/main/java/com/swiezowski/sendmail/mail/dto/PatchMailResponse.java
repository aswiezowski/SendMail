package com.swiezowski.sendmail.mail.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.Set;
import java.util.UUID;

@Value.Immutable
@JsonSerialize(as = ImmutablePatchMailResponse.class)
@JsonDeserialize(as = ImmutablePatchMailResponse.class)
public abstract class PatchMailResponse {

    public abstract Set<UUID> uuids();
}
