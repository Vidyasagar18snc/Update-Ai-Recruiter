package com.Vendor.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "interviews")
public class Interview {

    @Id
    private String id;

    // Candidate info
    private String candidateId;
    private String candidateName;

    // Interview timing
    private Date startTime;
    private Date endTime;

    // Google Meet link
    private String meetLink;

    // Interviewer info
    private String interviewerId;
    private String interviewerEmail;

    // Status (optional but useful)
    private String status; // SCHEDULED / COMPLETED / CANCELLED

    // Audit fields (optional but recommended)
    private Date createdAt = new Date();
}