package com.Vendor.repository;


import com.Vendor.model.Candidate;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CandidateRepository extends MongoRepository<Candidate, String> {
    Candidate findByEmail(String email);
}