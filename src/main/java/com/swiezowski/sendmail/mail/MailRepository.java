package com.swiezowski.sendmail.mail;

import com.swiezowski.sendmail.mail.entities.Mail;
import org.springframework.data.jpa.repository.JpaRepository;

interface MailRepository extends JpaRepository<Mail, Long> {

}
