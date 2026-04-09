//package com.Vendor.controller;
//
//
//import com.Vendor.entity.Vendor;
//import com.Vendor.service.VendorService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/vendors")
//@CrossOrigin(origins = "http://localhost:4200")
//public class VendorController {
//
//    @Autowired
//    private VendorService service;
//
//    @PostMapping("/upload")
//    public Map<String, Object> uploadFile(@RequestParam("file") MultipartFile file) {
//
//        try {
//            return service.processExcel(file);
//        } catch (Exception e) {
//            e.printStackTrace();
//
//            // Return proper error response instead of crash
//            return Map.of(
//                    "error", "Failed to process file",
//                    "message", e.getMessage()
//            );
//        }
//    }
//    @GetMapping("/list")
//    public List<Vendor> getAllVendors() {
//        return service.getAllVendors();
//    }
//    @GetMapping("/top-analysis")
//    public Map<String, Object> getTopAnalysis() {
//        return service.getTop3AndRemainingVendors();
//    }
//}