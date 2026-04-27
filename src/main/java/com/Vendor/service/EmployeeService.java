package com.Vendor.service;

import com.Vendor.model.Candidate;
import com.Vendor.model.Employee;
import com.Vendor.repository.CandidateRepository;
import com.Vendor.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service @AllArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepo;
    private  final CandidateRepository candidateRepo;

    public void createEmployee(String candidateId) {

        Candidate candidate = candidateRepo.findById(candidateId)
                .orElseThrow();

        if ("EMPLOYEE_CREATED".equals(candidate.getStatus())) return;

        Employee emp = new Employee();
        emp.setName(candidate.getName());
        String name = candidate.getName().toLowerCase().trim().replaceAll("\\s+", ".");
        String companyEmail = name + "@hginfotech.io";
        emp.setEmail(companyEmail);

        employeeRepo.save(emp);

        candidate.setStatus("EMPLOYEE_CREATED");
        candidateRepo.save(candidate);
    }
}