package com.Vendor.service;


import com.Vendor.entity.Job;
import com.Vendor.entity.JobRequest;
import com.Vendor.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;

    public Job createJob(JobRequest request) {

        Job job = Job.builder()
                .title(request.getTitle())
                .experience(request.getExperience())
                .description(request.getDescription())
                .skills(request.getSkills())
                .location(request.getLocation())
                .build();

        return jobRepository.save(job);
    }

    public Job getLatestJob() {
        return jobRepository.findAll()
                .stream()
                .reduce((first, second) -> second)
                .orElseThrow(() -> new RuntimeException("No job found"));
    }
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }
    public Job getJobByRole(String role) {

        List<Job> jobs = jobRepository.findByTitleIgnoreCase(role);

        if (jobs.isEmpty()) {
            return null;
        }

        // ✅ Pick best job (example: latest or highest experience)
        return jobs.get(0);
    }
}