package com.swiezowski.sendmail.mail;

import com.swiezowski.sendmail.mail.dto.MailStatusDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MailStatusConverter implements Converter<String, MailStatusDto> {

    @Override
    public MailStatusDto convert(String value) {
        return MailStatusDto.valueOf(value.toUpperCase());
    }
}

