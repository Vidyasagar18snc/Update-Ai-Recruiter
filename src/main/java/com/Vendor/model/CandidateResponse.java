package com.Vendor.model;



import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CandidateResponse {

    private String name;
    private int score;
    private String status;
    private String role;
    private List<String> skills;
    private List<String> extraSkills;
    private List<String> matchedSkills;

}