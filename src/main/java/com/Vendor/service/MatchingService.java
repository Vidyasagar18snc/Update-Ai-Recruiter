package com.Vendor.service;

import com.Vendor.model.Candidate;
import com.Vendor.model.Job;
import org.springframework.stereotype.Component;

@Component
public class MatchingService {

    public int calculateScore(Job job, Candidate candidate) {

        // ❌ No skills → reject directly
        if (candidate.getSkills() == null || candidate.getSkills().isEmpty()) {
            return 0;
        }

        int matchCount = 0;

        for (String skill : job.getSkills()) {
            if (candidate.getSkills().stream()
                    .anyMatch(s -> s.equalsIgnoreCase(skill))) {
                matchCount++;
            }
        }

        // ❌ No matching skills → reject
        if (matchCount == 0) {
            return 0;
        }

        int skillScore = (matchCount * 100) / job.getSkills().size();

        int expScore = (int) (((double) candidate.getExperience() / job.getExperience()) * 20);
        expScore = Math.min(expScore, 20);

        return Math.min(skillScore + expScore, 100);
    }
    public String getStatus(int score) {
        if (score == 0) return "Rejected"; // ✅ IMPORTANT
        if (score >= 80) return "Shortlisted";
        if (score >= 50) return "Pending";
        return "Rejected";
    }
}