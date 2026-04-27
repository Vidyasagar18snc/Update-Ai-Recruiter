package com.Vendor.util;

public class EmailMessageUtil {

    // ✅ OLD METHOD (kept for backward compatibility)
    public static String buildStatusMessage(String name, String status) {
        return buildStatusMessage(name, status, null);
    }

    // ✅ UPDATED METHOD (NOW SUPPORTS INTERVIEW LINK)
    public static String buildStatusMessage(String name, String status, String interviewLink) {

        if ("Shortlisted".equalsIgnoreCase(status)) {

            // 🔥 If interview link is available → include it
            if (interviewLink != null && !interviewLink.isEmpty()) {

                return "Hi " + name + ",\n\n"
                        + "🎉 Congratulations!\n"
                        + "You have been shortlisted for the interview.\n\n"

                        + "👉 Please join your virtual interview using the link below:\n\n"
                        + interviewLink + "\n\n"

                        + "⏰ Important Instructions:\n"
                        + "- Please join at the scheduled time\n"
                        + "- Allow camera & microphone access\n"
                        + "- Ensure stable internet connection\n\n"

                        + "We wish you all the best!\n\n"
                        + "Best Regards,\nHR Team";
            }

            // 🔥 Fallback (if link not passed)
            return "Hi " + name + ",\n\n"
                    + "🎉 Congratulations!\n"
                    + "You have been shortlisted for the interview.\n\n"
                    + "Our team will contact you soon with further details.\n\n"
                    + "Best Regards,\nHR Team";
        }

        else if ("Rejected".equalsIgnoreCase(status)) {
            return "Hi " + name + ",\n\n"
                    + "Thank you for applying.\n"
                    + "We regret to inform you that you are not selected.\n\n"
                    + "We wish you all the best for your future.\n\n"
                    + "Best Regards,\nHR Team";
        }

        else {
            return "Hi " + name + ",\n\n"
                    + "Your application is currently under review.\n"
                    + "We will update you soon.\n\n"
                    + "Best Regards,\nHR Team";
        }
    }

    // 🔥 EXISTING METHOD (UNCHANGED)
    public static String skillExpNotMatchMessage(String name) {

        return "Hi " + name + ",\n\n"
                + "Thank you for applying.\n\n"
                + "After reviewing your profile, we found that your skills and experience "
                + "do not match our current job requirements.\n\n"
                + "So, we are unable to shortlist your application at this time.\n\n"
                + "We encourage you to apply again in the future.\n\n"
                + "Best Regards,\nHR Team";
    }

    // 🔥 KEEP (optional if you still use test)
    public static String buildResultMessage(String name,
                                            int score,
                                            int total,
                                            double percentage,
                                            int rank,
                                            String status) {

        StringBuilder message = new StringBuilder();

        message.append("Hi ").append(name).append(",\n\n")
                .append("Your test has been successfully evaluated.\n\n")

                .append("📊 Test Result Summary:\n")
                .append("- Score: ").append(score).append(" / ").append(total).append("\n")
                .append("- Percentage: ").append(percentage).append("%\n")
                .append("- Rank: ").append(rank).append("\n")
                .append("- Status: ").append(status).append("\n\n");

        // ⭐ NEW BLOCK FOR TOP PERFORMER
        if ("TOP_PERFORMER".equalsIgnoreCase(status)) {

            message.append("🏆 Outstanding Performance!\n\n")
                    .append("Congratulations! You are among the top performers in this assessment.\n")
                    .append("Your performance has been exceptional, and you have demonstrated strong skills and potential.\n\n")
                    .append("We are excited to fast-track your application to the next round.\n")
                    .append("The upcoming stage will be a Face-to-Face interview.\n\n")
                    .append("Our team will share the interview details (date, time, and meeting link) shortly.\n")
                    .append("Stay tuned for further communication.\n");

        }
        // ✅ PASS (normal)
        else if ("PASS".equalsIgnoreCase(status)) {

            message.append("🎉 Congratulations! You have successfully cleared the test.\n\n")
                    .append("We are pleased to inform you that you have been shortlisted for the next round.\n")
                    .append("The next stage will be a Face-to-Face interview.\n\n")
                    .append("Our team will share the interview date, time, and venue details with you shortly.\n")
                    .append("Please keep an eye on your email for further communication.\n");

        }
        // ⚠️ REVIEW
        else if ("REVIEW".equalsIgnoreCase(status)) {

            message.append("👍 Good effort! Your performance is currently under review.\n\n")
                    .append("Our team will evaluate your results and get back to you soon with the next steps.\n");

        }
        // ❌ FAIL
        else {

            message.append("We appreciate your time and effort in completing the test.\n\n")
                    .append("After careful evaluation, we regret to inform you that you did not meet the qualifying criteria.\n\n")
                    .append("We wish you all the best in your career journey.\n");
        }

        message.append("\nBest Regards,\nHR Team");

        return message.toString();
    }
}