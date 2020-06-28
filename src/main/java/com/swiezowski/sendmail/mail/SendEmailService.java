package com.swiezowski.sendmail.mail;

import com.swiezowski.sendmail.mail.entities.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class SendEmailService {

    private JavaMailSender emailSender;

    @Autowired
    private SendEmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendMails(Collection<Mail> mail) {
        SimpleMailMessage[] simpleMails = mail.stream()
                .map(this::mapMailToSimpleMailMessage)
                .toArray(SimpleMailMessage[]::new);

        emailSender.send(simpleMails);
    }

    private SimpleMailMessage mapMailToSimpleMailMessage(Mail mail) {
        SimpleMailMessage simpleMail = new SimpleMailMessage();
        simpleMail.setFrom(mail.getSender());
        simpleMail.setTo(mail.getRecipients().toArray(new String[0]));
        simpleMail.setText(mail.getContent());
        return simpleMail;
    }
}
