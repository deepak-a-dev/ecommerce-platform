package com.icore.ecommerce_platform.serviceimpl;

import com.icore.ecommerce_platform.dto.MailBodyDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Sends simple text emails (e.g. password-reset OTPs) via the configured SMTP server.
 */
@Service
public class EmailServiceImpl {

    private final JavaMailSender javaMailSender;
    private final String fromAddress;

    public EmailServiceImpl(JavaMailSender javaMailSender,
                            @Value("${spring.mail.username}") String fromAddress) {
        this.javaMailSender = javaMailSender;
        this.fromAddress = fromAddress;
    }

    public void sendSimpleMessage(MailBodyDto mailBodyDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailBodyDto.to());
        message.setFrom(fromAddress);
        message.setSubject(mailBodyDto.subject());
        message.setText(mailBodyDto.text());

        javaMailSender.send(message);
    }
}
