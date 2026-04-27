package com.Vendor.repository;

import com.Vendor.model.Interview;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface InterviewRepository extends MongoRepository<Interview, String> {

    // 🔥 Check interviewer availability
    boolean existsByInterviewerIdAndStartTime(String interviewerId, Date startTime);

    // 🔥 Reminder logic
    List<Interview> findByStartTimeBetween(Date start, Date end);

    // 🔥 (Optional) Get all interviews for interviewer
    List<Interview> findByInterviewerId(String interviewerId);
}