package com.Vendor.repository;

import com.Vendor.model.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, String> {
    Optional<Employee> findByOfficialEmail(String officialEmail);
    Optional<Object> findByEmployeeId(String employeeId);
}