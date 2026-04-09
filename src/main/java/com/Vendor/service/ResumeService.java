package com.Vendor.service;

import com.Vendor.entity.Candidate;
import com.Vendor.entity.CandidateResponse;
import com.Vendor.entity.Job;
import com.Vendor.repository.CandidateRepository;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ResumeService {

    private final CandidateRepository candidateRepository;
    private final JobService jobService;
    private final EmailService emailService;

    public ResumeService(CandidateRepository candidateRepository,
                         JobService jobService,
                         EmailService emailService) {
        this.candidateRepository = candidateRepository;
        this.jobService = jobService;
        this.emailService = emailService;
    }

    // ✅ MAIN METHOD
    public CandidateResponse processResume(MultipartFile file, String roleFromHR) {

        String resumeText = extractText(file);

        String email = extractEmail(resumeText);
        String name = extractName(resumeText);

        System.out.println("✅ Name: " + name);
        System.out.println("✅ Email: " + email);
        System.out.println("✅ Role from HR: " + roleFromHR);

        Candidate candidate = new Candidate();
        candidate.setName(name);
        candidate.setEmail(email);

        String role;
        int score = 0;
        String status;

        // 🔥 STEP 1: VALIDATE ROLE
        Job job = jobService.getJobByRole(roleFromHR);

        if (job == null) {

            role = roleFromHR;
            status = "Rejected";
            System.out.println("❌ Role not found");

        } else {

            // 🔥 STEP 2: CALCULATE SCORE
            score = calculateFinalScore(job, resumeText);

            // 🔥 STEP 3: STATUS BASED ON SCORE
            if (score >= 90) {
                status = "Shortlisted";
            } else {
                status = "Rejected";
            }

            role = job.getTitle();
        }

        // ✅ SAVE ALWAYS
        candidate.setRole(role);
        candidate.setScore(score);
        candidate.setStatus(status);

        candidateRepository.save(candidate);

        // 📩 SEND EMAIL
        try {
            if (email != null) {
                if ("Rejected".equalsIgnoreCase(status)) {
                    emailService.sendStatusEmail(email, name, "NOT_MATCH");
                } else {
                    emailService.sendStatusEmail(email, name, status);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return CandidateResponse.builder()
                .name(name)
                .role(role)
                .score(score)
                .status(status)
                .build();
    }

    // 🔥 FINAL SCORE LOGIC (IMPORTANT)
    private int calculateFinalScore(Job job, String resumeText) {

        // ✅ Normalize resume
        resumeText = resumeText.toLowerCase()
                .replaceAll("[^a-z0-9 ]", " ")
                .replaceAll("\\s+", " ");

        int matchCount = 0;

        for (String skill : job.getSkills()) {

            String cleanSkill = skill.toLowerCase()
                    .replaceAll("[^a-z0-9 ]", " ")
                    .replaceAll("\\s+", " ")
                    .trim();

            if (resumeText.contains(cleanSkill)) {
                matchCount++;
            }
        }

        int totalSkills = job.getSkills().size();

        int candidateExp = extractExperience(resumeText);
        int requiredExp = job.getExperience();

        // 🔥 Skill Score
        double skillScore = ((double) matchCount / totalSkills) * 100;

        // 🔥 Experience Score
        double expScore = ((double) candidateExp / requiredExp) * 100;
        expScore = Math.min(expScore, 100);

        // 🔥 FINAL SCORE
        double finalScore = (skillScore * 0.7) + (expScore * 0.3);

        System.out.println("MatchCount: " + matchCount + "/" + totalSkills);
        System.out.println("Skill Score: " + skillScore);
        System.out.println("Exp Score: " + expScore);
        System.out.println("Final Score: " + finalScore);

        return (int) finalScore;
    }

    // ✅ EXPERIENCE EXTRACTION
    private int extractExperience(String text) {

        Pattern pattern = Pattern.compile(
                "(\\d+(\\.\\d+)?)\\s*(year|years|yr|yrs)",
                Pattern.CASE_INSENSITIVE
        );

        Matcher matcher = pattern.matcher(text);

        double maxExp = 0;

        while (matcher.find()) {
            double exp = Double.parseDouble(matcher.group(1));
            if (exp > maxExp) {
                maxExp = exp;
            }
        }

        return (int) Math.floor(maxExp);
    }

    // ✅ TEXT EXTRACTION
    private String extractText(MultipartFile file) {
        try {
            Tika tika = new Tika();
            return tika.parseToString(file.getInputStream());
        } catch (Exception e) {
            throw new RuntimeException("Error parsing resume", e);
        }
    }

    // ✅ EMAIL EXTRACTION
    private String extractEmail(String text) {

        Pattern pattern = Pattern.compile(
                "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
        );

        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            String email = matcher.group();
            if (!email.toLowerCase().contains("example")) {
                return email;
            }
        }

        return null;
    }

    // ✅ NAME EXTRACTION
    private String extractName(String text) {

        String[] lines = text.split("\\r?\\n");

        for (String line : lines) {
            if (!line.trim().isEmpty()
                    && line.trim().length() < 50
                    && !line.toLowerCase().contains("resume")
                    && !line.contains("@")) {

                return line.trim();
            }
        }

        return "Unknown Candidate";
    }

    // ✅ GET ALL
    public List<CandidateResponse> getAllCandidates() {

        return candidateRepository.findAll()
                .stream()
                .map(c -> CandidateResponse.builder()
                        .name(c.getName())
                        .role(c.getRole())
                        .score(c.getScore())
                        .status(c.getStatus())
                        .build())
                .collect(Collectors.toList());
    }
}