package com.Vendor.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document("Offer")
public class OfferRequestDTO {
    @Id
    private String CandidateId;
    private String name;
    private String email;
    private String role;
    private double salary;
    private String joiningDate;
    private String companyName;
    private String companyAddress;
    private String address;
    private String location;
    private String department;
    private String employmentType;
    private String hrEmail;
    private String hrPhone;
    private String hrSignatoryName;
    private String hrSignatoryTitle;
    private LocalDateTime createdAt;
    private LocalDateTime respondedAt;
}