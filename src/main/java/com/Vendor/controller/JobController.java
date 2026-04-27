package com.Vendor.controller;

import com.Vendor.model.Job;
import com.Vendor.service.EmployeeService;
import com.Vendor.service.JobService;
import com.Vendor.util.JDParsingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;
    private final JDParsingService jdParsingService;
    private final EmployeeService employeeService;

    @PostMapping("/jobs")
    public Job create(@RequestBody Job job
    ) {
        return jobService.createJob(job);
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

    @PostMapping("/upload-jd")
    public ResponseEntity<Job> uploadJD(
            @RequestParam("file") MultipartFile file) {

        Job job = jdParsingService.processJD(file);
        return ResponseEntity.ok(job);
    }
    @PutMapping("/jobs/{id}")
    public Job updateJob(
            @PathVariable String id,
            @RequestBody Job request
    ) {
        return jobService.updateJob(id, request);
    }
    @PostMapping("/create")
    public String createEmployee(@PathVariable String candidateId) {

        employeeService.createEmployee(candidateId);

        return "Employee created successfully";
    }
}
