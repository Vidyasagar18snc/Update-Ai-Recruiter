package com.Vendor.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "candidates")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Candidate {

    @Id
    private String id;

    private String name;
    private int experience;
    private List<String> skills;

    private int score;
    private String status;
    private String role;
    private String Email;
}
