package com.Vendor.service;

import com.Vendor.util.EmailMessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmailService{

    @Autowired
    private JavaMailSender mailSender;

    public void sendStatusEmail(String toEmail,String name,String status){

        String body;

        if("NOT_MATCH".equalsIgnoreCase(status)){
            body=EmailMessageUtil.skillExpNotMatchMessage(name);
        }else{
            body=EmailMessageUtil.buildStatusMessage(name,status);
        }

        sendEmail(toEmail,"Application Status Update",body);
    }

    public void sendInterviewEmail(String toEmail,String name,String interviewLink,Object interviewTime){

        if(toEmail==null||toEmail.isEmpty()){
            return;
        }

        String body=EmailMessageUtil.buildInterviewMessage(name,interviewLink,interviewTime);

        sendEmail(toEmail,"Interview Invitation",body);
    }

    public void sendResultEmail(String to,String name,int score,int total,double percentage,int rank,String status){

        if(to==null||to.isEmpty()){
            return;
        }

        String body=EmailMessageUtil.buildResultMessage(name,score,total,percentage,rank,status);

        sendEmail(to,"Test Result",body);
    }

    public void sendInterviewerNotification(String to,String candidateName,String role,String meetLink,LocalDateTime time){

        String body=EmailMessageUtil.buildInterviewerNotificationMessage(candidateName,role,meetLink,time);

        sendEmail(to,"Interview Assigned - "+candidateName,body);
    }

    public void sendInterviewerSlotSelectionMail(String to,String panelName,String panelPassword,String candidateName,String role){

        String body=EmailMessageUtil.buildPanelSlotSelectionMessage(to,panelName,panelPassword,candidateName,role);

        sendEmail(to,"Interview Panel Assignment",body);
    }

    public void sendOfferEmail(String email,String name,String url){

        String body=EmailMessageUtil.buildOfferEmailBody(name,url);

        sendEmail(email,"Offer Letter - Next Steps",body);
    }

    public void sendTestLink(String email,String testLink){

        if(email==null||email.isEmpty()){
            return;
        }

        String body=EmailMessageUtil.buildTestLinkMessage(testLink);

        sendEmail(email,"Online Test Invitation",body);
    }

    public void sendCandidateSlotSelectionMail(String to,String candidateName,String role,List<String> freeSlots,String accessToken){

        String body=EmailMessageUtil.buildCandidateSlotSelectionMessage(candidateName,role,freeSlots,accessToken);

        sendEmail(to,"Select Your Interview Slot",body);
    }

    private void sendEmail(String toEmail,String subject,String body){

        SimpleMailMessage message=new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);

        System.out.println("Email sent successfully!");
    }
    public void sendHrOfferAcceptedMail(
            String hrEmail,
            String candidateName,
            String role
    ) {

        String body = String.format(
                EmailMessageUtil.HR_OFFER_ACCEPTED_BODY,
                candidateName,
                role
        );

        sendEmail(
                hrEmail,
                EmailMessageUtil.HR_OFFER_ACCEPTED_SUBJECT,
                body
        );
    }

    public void sendHrOfferRejectedMail(
            String hrEmail,
            String candidateName,
            String role
    ) {

        String body = String.format(
                EmailMessageUtil.HR_OFFER_REJECTED_BODY,
                candidateName,
                role
        );

        sendEmail(
                hrEmail,
                EmailMessageUtil.HR_OFFER_REJECTED_SUBJECT,
                body
        );
    }

    public void sendCandidateOfferAcceptedMail(
            String email,
            String candidateName,
            String candidateId
    ) {

        String onboardingLink =
                "http://localhost:4200/uploaddocuments/" + candidateId;

        String body = String.format(
                EmailMessageUtil.CANDIDATE_OFFER_ACCEPTED_BODY,
                candidateName,
                onboardingLink
        );

        sendEmail(
                email,
                EmailMessageUtil.CANDIDATE_OFFER_ACCEPTED_SUBJECT,
                body
        );
    }

    public void sendCandidateOfferRejectedMail(
            String email,
            String candidateName
    ) {

        String body = String.format(
                EmailMessageUtil.CANDIDATE_OFFER_REJECTED_BODY,
                candidateName
        );

        sendEmail(
                email,
                EmailMessageUtil.CANDIDATE_OFFER_REJECTED_SUBJECT,
                body
        );
    }

    public void sendHrDocumentUploadedMail(
            String hrEmail,
            String candidateName,
            String documentType
    ) {

        String body = String.format(
                EmailMessageUtil.DOCUMENT_UPLOADED_BODY,
                candidateName,
                documentType
        );

        sendEmail(
                hrEmail,
                EmailMessageUtil.DOCUMENT_UPLOADED_SUBJECT,
                body
        );
    }

    public void sendDocumentRejectedMail(
            String email,
            String candidateName,
            String documentType,
            String remarks
    ) {

        String body = String.format(
                EmailMessageUtil.DOCUMENT_REJECTED_BODY,
                candidateName,
                documentType,
                remarks
        );

        sendEmail(
                email,
                EmailMessageUtil.DOCUMENT_REJECTED_SUBJECT,
                body
        );
    }

    public void sendAllDocumentsVerifiedMail(
            String email,
            String candidateName
    ) {

        String body = String.format(
                EmailMessageUtil.ALL_DOCUMENT_VERIFIED_BODY,
                candidateName
        );

        sendEmail(
                email,
                EmailMessageUtil.ALL_DOCUMENT_VERIFIED_SUBJECT,
                body
        );
    }

    public void sendEmployeeCredentialsMail(
            String to,
            String candidateName,
            String employeeId,
            String officialEmail,
            String password
    ) {

        String body = String.format(
                EmailMessageUtil.EMPLOYEE_CREDENTIALS_BODY,
                candidateName,
                employeeId,
                officialEmail,
                password
        );

        sendEmail(
                to,
                EmailMessageUtil.EMPLOYEE_CREDENTIALS_SUBJECT,
                body
        );
    }
}