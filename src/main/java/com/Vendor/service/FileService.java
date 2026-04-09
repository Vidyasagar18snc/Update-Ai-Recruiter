//package com.Vendor.service;
//
//import com.Vendor.entity.Vendor;
//import com.Vendor.repository.VendorRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class FileService {
//
//    private final VendorRepository repository;
//    private final AIService aiService;
//
//    public String processFile(MultipartFile file) {
//
//        List<Vendor> vendors = new ArrayList<>();
//
//        try (BufferedReader reader =
//                     new BufferedReader(new InputStreamReader(file.getInputStream()))) {
//
//            String line;
//
//            // ✅ Skip header if present
//            boolean isFirstLine = true;
//
//            while ((line = reader.readLine()) != null) {
//
//                if (isFirstLine && line.toLowerCase().contains("name")) {
//                    isFirstLine = false;
//                    continue;
//                }
//
//                String[] data = line.split(",");
//
//                // ✅ Validate data length
//                if (data.length < 4) {
//                    continue;
//                }
//
//                try {
//                    Vendor vendor = Vendor.builder()
//                            .name(data[0].trim())
//                            .category(data[1].trim())
//                            .rating(Double.parseDouble(data[2].trim()))
//                            .status(data[3].trim())
//                            .build();
//
//                    vendors.add(vendor);
//
//                } catch (Exception ex) {
//                    // Skip bad row
//                    System.out.println("Invalid row skipped: " + line);
//                }
//            }
//
//            // ✅ Save to MongoDB
//            repository.saveAll(vendors);
//
//        } catch (Exception e) {
//            throw new RuntimeException("Error reading file: " + e.getMessage());
//        }
//
//        // ❌ If no data
//        if (vendors.isEmpty()) {
//            return "No valid vendor data found in file";
//        }
//
//        // ✅ Send structured data to AI
//        return aiService.findBestVendor(vendors);
//    }
//}