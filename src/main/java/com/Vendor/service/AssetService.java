package com.Vendor.service;

import com.Vendor.dto.AssetAssignment;
import com.Vendor.model.Asset;

import com.Vendor.model.Employee;
import com.Vendor.repository.AssetAssignmentRepository;
import com.Vendor.repository.AssetRepository;

import com.Vendor.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service @RequiredArgsConstructor
public class AssetService {

    private final AssetRepository assetRepository;
    private final EmployeeRepository employeeRepository;
    private final AssetAssignmentRepository assetAssignmentRepository;

    public Asset createAsset(Asset asset) {
        asset.setStatus("AVAILABLE");
        return assetRepository.save(asset);
    }
    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }
    public String assignAsset(

            String assetId,

            String employeeId
    ) {

        // FIND ASSET

        Asset asset =

                assetRepository
                        .findByAssetId(
                                assetId
                        )
                        .orElseThrow(() ->

                                new RuntimeException(
                                        "Asset not found"
                                )
                        );

        // CHECK AVAILABLE

        if (

                !asset.getStatus()
                        .equals("AVAILABLE")
        ) {

            throw new RuntimeException(
                    "Asset already assigned"
            );
        }

        // FIND EMPLOYEE

        Employee employee = (Employee) employeeRepository.findByEmployeeId(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee not found"));

        // CREATE ASSIGNMENT

        AssetAssignment assignment =
                new AssetAssignment();

        assignment.setEmployeeId(
                employee.getEmployeeId()
        );

        assignment.setEmployeeName(
                employee.getEmployeeName()
        );

        assignment.setAssetId(
                asset.getAssetId()
        );

        assignment.setAssetName(
                asset.getAssetName()
        );

        assignment.setBrand(
                asset.getBrand()
        );

        assignment.setSerialNumber(
                asset.getSerialNumber()
        );

        assignment.setAssignedDate(
                LocalDate.now()
        );

        assignment.setStatus(
                "ASSIGNED"
        );

        assetAssignmentRepository.save(
                assignment
        );

        // UPDATE ASSET STATUS

        asset.setStatus(
                "ASSIGNED"
        );

        assetRepository.save(
                asset
        );

        return "Asset Assigned Successfully";
    }
    public List<AssetAssignment>
    getEmployeeAssets(
            String employeeId
    ) {

        return assetAssignmentRepository
                .findByEmployeeId(
                        employeeId
                );
    }
    public String returnAsset(

            String assetId,

            String employeeId
    ) {

        // FIND ASSIGNMENT

        AssetAssignment assignment =

                assetAssignmentRepository

                        .findByAssetIdAndEmployeeId(

                                assetId,

                                employeeId
                        )

                        .orElseThrow(() ->

                                new RuntimeException(

                                        "Assignment not found"
                                )
                        );

        // ALREADY RETURNED

        if (

                assignment.getStatus()
                        .equals("RETURNED")
        ) {

            throw new RuntimeException(

                    "Asset already returned"
            );
        }

        // UPDATE ASSIGNMENT

        assignment.setStatus(
                "RETURNED"
        );

        assignment.setReturnedDate(
                LocalDate.now()
        );

        assetAssignmentRepository.save(
                assignment
        );

        // UPDATE ASSET

        Asset asset =

                assetRepository
                        .findByAssetId(
                                assetId
                        )
                        .orElseThrow(() ->

                                new RuntimeException(
                                        "Asset not found"
                                )
                        );

        asset.setStatus(
                "AVAILABLE"
        );

        assetRepository.save(
                asset
        );

        return "Asset Returned Successfully";
    }
    public Asset updateAsset(String id, Asset updatedAsset) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asset not found"));
        asset.setAssetName(updatedAsset.getAssetName());
        asset.setCategory(updatedAsset.getCategory());
        asset.setBrand(updatedAsset.getBrand());
        asset.setSerialNumber(updatedAsset.getSerialNumber());
        return assetRepository.save(asset);
    }
    public String deleteAsset(String id) {

        Asset asset = assetRepository.findById(id).orElseThrow(() ->
                        new RuntimeException("Asset not found")
                );

        assetRepository.delete(asset);
        return "Asset Deleted Successfully";
    }
}