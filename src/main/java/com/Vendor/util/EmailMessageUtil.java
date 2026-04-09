package com.Vendor.util;

public class EmailMessageUtil {

    // ✅ Existing method
    public static String buildStatusMessage(String name, String status) {

        if ("Shortlisted".equalsIgnoreCase(status)) {
            return "Hi " + name + ",\n\n"
                    + "🎉 Congratulations!\n"
                    + "You have been shortlisted for the next round.\n\n"
                    + "Our team will contact you soon.\n\n"
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

    // 🔥 NEW METHOD (ROLE NOT FOUND)
    public static String skillExpNotMatchMessage(String name) {

        return "Hi " + name + ",\n\n"
                + "Thank you for applying.\n\n"
                + "After reviewing your profile, we found that your skills and experience "
                + "do not match our current job requirements.\n\n"
                + "So, we are unable to shortlist your application at this time.\n\n"
                + "We encourage you to apply again in the future.\n\n"
                + "Best Regards,\nHR Team";
    }
}