package com.example.instagram.service;

import com.example.instagram.dto.internal.EmailDetails;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Value("${spring.mail.username}")
    private String sender;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public String sendMail(EmailDetails emailDetails) {
        try {
            MimeMessage mailMessage = javaMailSender.createMimeMessage();

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mailMessage, true);

            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(emailDetails.getRecipient());
            mimeMessageHelper.setSubject(emailDetails.getSubject());
            mimeMessageHelper.setText(emailDetails.getMsgBody(), true);

            ClassPathResource imageResource = new ClassPathResource("images/icon.png");
            mimeMessageHelper.addInline("instagramLogo", imageResource);

            javaMailSender.send(mailMessage);
            return "Mail Sent Successfully.";
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            return "Error while Sending Mail.";
        }
    }
}
