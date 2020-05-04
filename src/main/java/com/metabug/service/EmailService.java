package com.metabug.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    @Value("${${spring.profiles.active}-url}")
    private String ticketUrl;

    @Autowired
    public EmailService(final JavaMailSender javaMailSender,
                        final TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    public void send(final long ticketId, final String ticketTitle,
                     final String ticketStatus, final String developer,
                     final String recipient) {
        final Context context = new Context();
        context.setVariable("header", "Issue status has changed");
        context.setVariable("title", "Status of issue [" +
                ticketTitle + "] changed to [" + ticketStatus + "] by [" + developer + "]");
        context.setVariable("ticketUrl", ticketUrl + ticketId);

        final String body = templateEngine.process("email-template", context);

        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            mimeMessage.setSubject("[" + ticketId + "] Issue status has changed");
            final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setTo(recipient);
            helper.setText(body, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
