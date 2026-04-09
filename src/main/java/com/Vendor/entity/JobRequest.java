package com.Vendor.entity;

import lombok.Data;
import java.util.List;

@Data
public class JobRequest {
    private String title;
    private int experience;
    private List<String> skills;
    private String description;
    private String location;
}