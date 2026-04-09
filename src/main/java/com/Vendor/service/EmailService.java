package com.Vendor.service;

import com.Vendor.util.EmailMessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendStatusEmail(String toEmail, String name, String status) {

        System.out.println("Sending email to: " + toEmail);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Application Status Update");

        String body;

        // 🔥 CUSTOM LOGIC
        if ("NOT_MATCH".equalsIgnoreCase(status)) {
            body = EmailMessageUtil.skillExpNotMatchMessage(name);
        } else {
            body = EmailMessageUtil.buildStatusMessage(name, status);
        }

        message.setText(body);

        mailSender.send(message);

        System.out.println("✅ Email sent successfully!");
    }
}