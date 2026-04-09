package com.Vendor.controller;

import com.Vendor.entity.CandidateResponse;
import com.Vendor.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping("/upload")
    public CandidateResponse upload(
            @RequestParam MultipartFile file,
            @RequestParam String role) {

        return resumeService.processResume(file, role);
    }

    @GetMapping("/resume")
    public List<CandidateResponse> getAll() {
        return resumeService.getAllCandidates();
    }
}