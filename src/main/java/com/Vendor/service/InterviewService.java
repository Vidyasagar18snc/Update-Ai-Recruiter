package com.Vendor.service;

import com.Vendor.config.GoogleCalendarConfig;
import com.Vendor.config.InterviewerConfig;
import com.Vendor.model.Interview;
import com.Vendor.repository.InterviewRepository;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class InterviewService {

    private final GoogleCalendarConfig config;
    private final InterviewRepository interviewRepo;
    private final InterviewerConfig interviewerConfig;

    private static final int INTERVIEW_DURATION = 60;
    private static final int BREAK_DURATION = 10;

    public synchronized Interview scheduleInterview(String candidateName, String candidateId) {

        try {
            Calendar service = config.getCalendarService();

            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

            // 🔥 Start from tomorrow 10 AM
            cal.add(java.util.Calendar.DATE, 1);
            cal.set(java.util.Calendar.HOUR_OF_DAY, 10);
            cal.set(java.util.Calendar.MINUTE, 0);

            while (true) {

                Date start = cal.getTime();

                // ❌ Skip Saturday & Sunday
                int day = cal.get(java.util.Calendar.DAY_OF_WEEK);
                if (day == java.util.Calendar.SATURDAY || day == java.util.Calendar.SUNDAY) {
                    cal.add(java.util.Calendar.DATE, 1);
                    cal.set(java.util.Calendar.HOUR_OF_DAY, 10);
                    cal.set(java.util.Calendar.MINUTE, 0);
                    continue;
                }

                // 🔍 Find free interviewer
                InterviewerConfig.Interviewer selected = null;

                for (InterviewerConfig.Interviewer i : interviewerConfig.getInterviewers()) {

                    boolean busy = interviewRepo
                            .existsByInterviewerIdAndStartTime(i.getId(), start);

                    if (!busy) {
                        selected = i;
                        break;
                    }
                }

                if (selected != null) {

                    // ⏱️ End time
                    java.util.Calendar endCal = java.util.Calendar.getInstance();
                    endCal.setTime(start);
                    endCal.add(java.util.Calendar.MINUTE, INTERVIEW_DURATION);
                    Date end = endCal.getTime();

                    // 🎥 Google Meet
                    Event event = new Event()
                            .setSummary("Interview - " + candidateName)
                            .setStart(new EventDateTime()
                                    .setDateTime(new com.google.api.client.util.DateTime(start))
                                    .setTimeZone("Asia/Kolkata"))
                            .setEnd(new EventDateTime()
                                    .setDateTime(new com.google.api.client.util.DateTime(end))
                                    .setTimeZone("Asia/Kolkata"));

                    ConferenceData conf = new ConferenceData()
                            .setCreateRequest(new CreateConferenceRequest()
                                    .setRequestId(UUID.randomUUID().toString())
                                    .setConferenceSolutionKey(
                                            new ConferenceSolutionKey().setType("hangoutsMeet")));

                    event.setConferenceData(conf);

                    Event created = service.events()
                            .insert("primary", event)
                            .setConferenceDataVersion(1)
                            .execute();

                    // 💾 Save interview
                    Interview interview = new Interview();
                    interview.setCandidateId(candidateId);
                    interview.setCandidateName(candidateName);
                    interview.setStartTime(start);
                    interview.setEndTime(end);
                    interview.setMeetLink(created.getHangoutLink());
                    interview.setInterviewerId(selected.getId());
                    interview.setInterviewerEmail(selected.getEmail());

                    return interviewRepo.save(interview);
                }

                // ❌ All busy → next slot (60 + break)
                cal.add(java.util.Calendar.MINUTE, INTERVIEW_DURATION + BREAK_DURATION);
            }

        } catch (Exception e) {
            throw new RuntimeException("Scheduling failed", e);
        }
    }
}