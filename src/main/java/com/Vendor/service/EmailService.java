package com.Vendor.service;

import com.Vendor.util.EmailMessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // ✅ OLD METHOD (kept)
    public void sendStatusEmail(String toEmail, String name, String status) {

        System.out.println("Sending email to: " + toEmail);

        String body;

        if ("NOT_MATCH".equalsIgnoreCase(status)) {
            body = EmailMessageUtil.skillExpNotMatchMessage(name);
        } else {
            body = EmailMessageUtil.buildStatusMessage(name, status);
        }

        sendEmail(toEmail, "Application Status Update", body);
    }

    // ❌ OLD TEST METHOD (KEEP or REMOVE based on need)
    public void sendStatusEmailWithLink(String toEmail, String name, String status, String testLink) {

        System.out.println("Sending shortlisted email with link to: " + toEmail);

        String body = EmailMessageUtil.buildStatusMessage(name, status, testLink);

        sendEmail(toEmail, "Application Status Update", body);
    }

    // 🔥 ✅ NEW METHOD (INTERVIEW EMAIL)
    public void sendInterviewEmail(String toEmail,
                                   String name,
                                   String interviewLink,
                                   Object interviewTime) {

        if (toEmail == null || toEmail.isEmpty()) {
            System.out.println("⚠️ Email is null, skipping...");
            return;
        }

        System.out.println("Sending interview email to: " + toEmail);

        // ✅ Use updated template
        String body = EmailMessageUtil.buildStatusMessage(
                name,
                "Shortlisted",
                interviewLink
        );

        //  Optional: append time
        body += "\n\n📅 Interview Time: " + interviewTime + "\n";

        sendEmail(toEmail, "Interview Invitation", body);
    }

    //  RESULT EMAIL (UNCHANGED)
    public void sendResultEmail(String to,
                                String name,
                                int score,
                                int total,
                                double percentage,
                                int rank,
                                String status) {

        if (to == null || to.isEmpty()) {
            System.out.println("⚠️ Email is null, skipping email send");
            return;
        }

        System.out.println("Sending result email to: " + to);

        String body = EmailMessageUtil.buildResultMessage(
                name,
                score,
                total,
                percentage,
                rank,
                status
        );

        sendEmail(to, "Test Result", body);
    }

    //  COMMON METHOD (UPDATED WITH SUBJECT SUPPORT)
    private void sendEmail(String toEmail, String subject, String body) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);

        System.out.println("✅ Email sent successfully!");
    }

    public void sendHRNotification(String hrEmail,
                                   String candidateName,
                                   String role,
                                   String meetLink,
                                   LocalDateTime time) {

        String subject = "🚨 Candidate Shortlisted - Interview Required";

        String body = "Dear HR,\n\n" +
                "A candidate has been shortlisted.\n\n" +
                "👤 Name: " + candidateName + "\n" +
                "💼 Role: " + role + "\n" +
                "📅 Interview Time: " + time + "\n\n" +
                "🎥 Join Interview:\n" + meetLink + "\n\n" +
                "Please take the interview.\n\n" +
                "AI Recruiter System";

        sendEmail(hrEmail, subject, body);
    }
    public void sendInterviewerNotification(
            String to,
            String candidateName,
            String role,
            String meetLink,
            LocalDateTime time
    ) {

        String subject = "Interview Assigned - " + candidateName;

        String body = "Hello,\n\n"
                + "You have been assigned an interview.\n\n"
                + "Candidate: " + candidateName + "\n"
                + "Role: " + role + "\n"
                + "Time: " + time + "\n"
                + "Meet Link: " + meetLink + "\n\n"
                + "Thanks";

        sendEmail(to, subject, body);
    }

    public void sendOfferEmail(String email, String name, String url) {

        String subject = "Offer Letter - Next Steps";

        String body = buildOfferEmailBody(name, url);

        sendEmail(email, subject, body);
    }

    private String buildOfferEmailBody(String name, String url) {

        return "Dear " + name + ",\n\n"
                + "We are pleased to inform you that your offer letter has been successfully generated.\n\n"
                + "You can download your offer letter using the link below:\n"
                + url + "\n\n"
                + "Please review the document carefully and feel free to reach out in case of any queries.\n\n"
                + "We look forward to having you on board.\n\n"
                + "Best regards,\n"
                + "HR Team";
    }

    public void sendTestLink(String email, String testLink) {

        if (email == null || email.isEmpty()) {
            System.out.println("⚠️ Email is null, skipping...");
            return;
        }

        System.out.println("📧 Sending test link to: " + email);

        String subject = "🧠 Online Test Invitation";

        String body = "Dear Candidate,\n\n"
                + "Congratulations! You have been shortlisted for the next round.\n\n"
                + "Please complete the online assessment using the link below:\n\n"
                + testLink + "\n\n"
                + "⏳ Note:\n"
                + "- This test link is valid for 48 hours.\n"
                + "- The test can be attempted only once.\n"
                + "- Please ensure a stable internet connection.\n\n"
                + "We wish you all the best!\n\n"
                + "Best Regards,\n"
                + "HR Team";

        sendEmail(email, subject, body);
    }
}