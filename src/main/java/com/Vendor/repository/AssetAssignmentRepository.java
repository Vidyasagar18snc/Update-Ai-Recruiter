package com.Vendor.repository;


import com.Vendor.dto.AssetAssignment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface AssetAssignmentRepository extends MongoRepository<AssetAssignment,String> {

    List<AssetAssignment> findByEmployeeId(String employeeId);
    Optional<AssetAssignment> findByAssetIdAndEmployeeId(String assetId, String employeeId);
    
}