package com.Vendor.entity;



import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CandidateResponse {

    private String name;
    private int score;
    private String status;
    private String role;
}