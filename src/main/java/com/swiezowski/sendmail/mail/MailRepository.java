package com.swiezowski.sendmail.mail;

import com.swiezowski.sendmail.mail.entities.Mail;
import com.swiezowski.sendmail.mail.entities.MailStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface MailRepository extends JpaRepository<Mail, Long> {

    Optional<Mail> findByUuid(UUID uuid);

    List<Mail> findByStatus(MailStatus status);
}
