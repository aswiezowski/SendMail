INSERT INTO mail(id, uuid, subject, sender, content, status)
VALUES (1, '1a202ff40131431e8342a6c3abec6ec1', 'predefined mail subject', 'me@example.com', 'content', 0);

INSERT INTO mail_recipients(mail_id, recipients) VALUES (1, 'recipient1@company.com');
INSERT INTO mail_recipients(mail_id, recipients) VALUES (1, 'recipient2@company.com');

ALTER SEQUENCE hibernate_sequence RESTART WITH 2