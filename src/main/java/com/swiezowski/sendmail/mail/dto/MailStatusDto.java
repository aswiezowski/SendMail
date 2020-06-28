package com.swiezowski.sendmail.mail.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum MailStatusDto {

    @JsonProperty("pending")
    PENDING,
    @JsonProperty("sent")
    SENT;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
