
package com.Vendor.service;

import com.Vendor.dto.GenerateLinkRequestDTO;
import com.Vendor.model.Candidate;
import com.Vendor.model.CandidateResponse;
import com.Vendor.model.Interview;
import com.Vendor.model.Job;
import com.Vendor.repository.CandidateRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final CandidateRepository candidateRepository;
    private final JobService jobService;
    private final EmailService emailService;
    private final TestService testService;
    private final InterviewService interviewService;

    @Value("${app.hr.email}")
    private String hrEmail;



    public CandidateResponse processResume(MultipartFile file, String roleFromHR) {

        String resumeText = extractText(file);

        String email = extractEmail(resumeText);
        String name = extractName(resumeText);

        Candidate candidate = new Candidate();
        candidate.setName(name);
        candidate.setEmail(email);

        String role;
        int score = 0;
        String status;

        List<String> resumeSkills = extractSkills(resumeText);

        List<String> matchedSkills = new ArrayList<>();
        List<String> extraSkills = new ArrayList<>();

        Job job = jobService.getJobByRole(roleFromHR);

        if (job == null) {
            role = roleFromHR;
            status = "Rejected";
            extraSkills = resumeSkills;
        } else {

            matchedSkills = resumeSkills.stream()
                    .filter(skill -> job.getSkills().stream()
                            .anyMatch(jd -> jd.equalsIgnoreCase(skill)))
                    .collect(Collectors.toList());

            extraSkills = resumeSkills.stream()
                    .filter(skill -> job.getSkills().stream()
                            .noneMatch(jd -> jd.equalsIgnoreCase(skill)))
                    .collect(Collectors.toList());

            score = calculateFinalScore(job, resumeText);

            if (score >= 85) status = "Shortlisted";
            else if (score >= 70) status = "Review";
            else status = "Rejected";

            role = job.getTitle();
        }

        candidate.setRole(role);
        candidate.setScore(score);
        candidate.setStatus(status);
        candidate.setSkills(resumeSkills);
        candidate.setMatchedSkills(matchedSkills);
        candidate.setExtraSkills(extraSkills);
        candidate.setExperience(extractExperience(resumeText));

        candidate = candidateRepository.save(candidate);

        // ================= EMAIL =================
        try {
            if (email != null) {

                if ("Rejected".equalsIgnoreCase(status)) {

                    emailService.sendStatusEmail(email, name, "NOT_MATCH");

                } else if ("Shortlisted".equalsIgnoreCase(status)) {

                    if (candidate.getExperience() <= 0.5) {

                        // ✅ FIXED DTO USAGE
                        GenerateLinkRequestDTO request = new GenerateLinkRequestDTO();
                        request.setCandidateId(candidate.getId());
                        request.setTestId("GENERAL_TEST");

                        String testLink = testService.generateTestLink(request);

                        candidate.setInterviewLink(testLink);
                        candidateRepository.save(candidate);

                        emailService.sendTestLink(email, testLink);

                        System.out.println("🧠 Fresher → Test link generated");

                    } else {

                        Interview interview = interviewService
                                .scheduleInterview(name, candidate.getId());

                        candidate.setInterviewLink(interview.getMeetLink());
                        candidateRepository.save(candidate);

                        LocalDateTime interviewTime = interview.getStartTime()
                                .toInstant()
                                .atZone(ZoneId.of("Asia/Kolkata"))
                                .toLocalDateTime();

                        emailService.sendInterviewEmail(
                                email, name,
                                interview.getMeetLink(),
                                interviewTime
                        );

                        emailService.sendHRNotification(
                                hrEmail, name, role,
                                interview.getMeetLink(),
                                interviewTime
                        );

                        emailService.sendInterviewerNotification(
                                interview.getInterviewerEmail(),
                                name, role,
                                interview.getMeetLink(),
                                interviewTime
                        );

                        System.out.println("🎯 Experienced → Interview scheduled");
                    }

                } else {
                    emailService.sendStatusEmail(email, name, "UNDER_REVIEW");
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
                .matchedSkills(matchedSkills)
                .extraSkills(extraSkills)
                .build();
    }    private int calculateFinalScore(Job job, String resumeText) {

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

        double candidateExp = extractExperience(resumeText);
        double requiredExp = job.getExperience();

        double skillScore = ((double) matchCount / totalSkills) * 100;

        double expScore;

        // ✅ tolerance logic
        if (candidateExp >= requiredExp * 0.9) {
            expScore = 100;
        } else {
            expScore = (candidateExp / requiredExp) * 100;
        }

        expScore = Math.min(expScore, 100);

        double finalScore = (skillScore * 0.7) + (expScore * 0.3);

        return (int) finalScore;
    }

    // ================= EXPERIENCE =================
    private double extractExperience(String text) {

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

        return maxExp;
    }

    // ================= SKILL EXTRACTION =================
    private List<String> extractSkills(String resumeText) {

        List<String> skills = new ArrayList<>();

        resumeText = resumeText.toLowerCase();

        // ✅ STRICT: stop at double newline (section boundary)
        Pattern pattern = Pattern.compile(
                "(skills|technical skills|technologies|tech stack)\\s*[:\\-]?\\s*(.*?)\\n\\s*\\n",
                Pattern.CASE_INSENSITIVE | Pattern.DOTALL
        );

        Matcher matcher = pattern.matcher(resumeText);

        if (matcher.find()) {

            String skillsBlock = matcher.group(2);

            String[] tokens = skillsBlock.split("[,\\n•|]");

            for (String token : tokens) {

                String skill = token.trim();

                // ✅ CLEAN
                skill = skill.replaceAll("[^a-zA-Z0-9+#. ]", "").trim();

                // ✅ STRICT FILTER
                if (isValidSkill(skill)) {
                    skills.add(skill);
                }
            }
        }

        return skills.stream().distinct().collect(Collectors.toList());
    }

    private boolean isValidSkill(String skill) {

        if (skill == null || skill.isEmpty()) return false;

        // ❌ remove long sentences
        if (skill.split(" ").length > 3) return false;

        // ❌ remove emails / links
        if (skill.contains("@") || skill.contains(".com") || skill.contains("http")) return false;

        // ❌ remove common non-skill words
        List<String> ignore = List.of(
                "summary", "profile", "experience", "education",
                "project", "developer", "engineer", "skills"
        );

        if (ignore.contains(skill)) return false;

        return true;
    }


    // ================= UTIL =================
    private String extractText(MultipartFile file) {
        try {
            Tika tika = new Tika();
            return tika.parseToString(file.getInputStream());
        } catch (Exception e) {
            throw new RuntimeException("Error parsing resume", e);
        }
    }

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

    public List<CandidateResponse> getAllCandidates() {

        return candidateRepository.findAll()
                .stream()
                .map(c -> CandidateResponse.builder()
                        .name(c.getName())
                        .role(c.getRole())
                        .score(c.getScore())
                        .status(c.getStatus())

                        .skills(   // ✅ ADD THIS
                                c.getSkills() != null ? c.getSkills() : new ArrayList<>()
                        )

                        .matchedSkills(
                                c.getMatchedSkills() != null ? c.getMatchedSkills() : new ArrayList<>()
                        )
                        .extraSkills(
                                c.getExtraSkills() != null ? c.getExtraSkills() : new ArrayList<>()
                        )
                        .build())
                .collect(Collectors.toList());
    }
    private List<String> calculateMatchedSkills(List<String> candidateSkills,
                                                List<String> requiredSkills) {

        if (candidateSkills == null || requiredSkills == null) {
            return new ArrayList<>();
        }

        return candidateSkills.stream()
                .filter(skill ->
                        requiredSkills.stream()
                                .anyMatch(req -> req.equalsIgnoreCase(skill))
                )
                .collect(Collectors.toList());
    }
}