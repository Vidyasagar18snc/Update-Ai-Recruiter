package com.Vendor.controller;

import com.Vendor.entity.Job;
import com.Vendor.entity.JobRequest;
import com.Vendor.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping("/jobs")
    public Job create(@RequestBody JobRequest request) {
        return jobService.createJob(request);
    }
    @GetMapping("/latest")
    public ResponseEntity<?> getLatestJob() {
        try {
            return ResponseEntity.ok(jobService.getLatestJob());
        } catch (Exception e) {
            return ResponseEntity.status(404).body("No job found");
        }
    }
    @GetMapping("/jobs")
    public List<Job> getAllJobs() {
        return jobService.getAllJobs();
    }
}