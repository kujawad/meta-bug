package com.metabug.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;

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

    public void sendAttachment(final long ticketId, final String ticketTitle,
                               final String developer, final String recipient,
                               final MultipartFile file) {
        final Context context = new Context();
        context.setVariable("header", "[" + developer + "] sent an attachment");
        context.setVariable("title", "Attachment to issue [" +
                ticketTitle + "] added by [" + developer + "]");
        context.setVariable("ticketUrl", ticketUrl + ticketId);

        final String body = templateEngine.process("email-template", context);

        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            mimeMessage.setSubject("[" + ticketId + "] Developer has sent an attachment");
            final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setTo(recipient);

            final Multipart multipart = new MimeMultipart();
            final MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            attachmentBodyPart.setContent(file.getBytes(), file.getContentType());
            attachmentBodyPart.setFileName(file.getOriginalFilename());
            attachmentBodyPart.setDisposition(Part.ATTACHMENT);
            multipart.addBodyPart(attachmentBodyPart);

            final MimeBodyPart htmlBodyPart = new MimeBodyPart();
            htmlBodyPart.setContent(body, "text/html");
            multipart.addBodyPart(htmlBodyPart);

            mimeMessage.setContent(multipart);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
