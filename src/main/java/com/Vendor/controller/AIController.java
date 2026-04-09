//package com.Vendor.controller;
//
//import com.Vendor.entity.VendorAnalysisDTO;
//import com.Vendor.service.AIService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api")
//@CrossOrigin(origins = "http://localhost:54110")
//@RequiredArgsConstructor
//public class AIController {
//
//    private final AIService aiService;
//
//    @GetMapping("/ask")
//    public String chat(@RequestParam String question) {
//        return aiService.ask(question);
//    }
//
//    @GetMapping("/top-vendors")
//    public Map<String, Object> getTopVendors() {
//
//        List<VendorAnalysisDTO> all = aiService.getTopVendors();
//
//        return Map.of(
//                "allVendors", all,
//                "topVendors", all.stream().limit(3).toList()
//        );
//    }
//
//    @PostMapping("/analyze")
//    public Map<String, Object> analyze(@RequestParam("file") MultipartFile file) {
//
//        List<VendorAnalysisDTO> all = aiService.analyzeFile(file);
//
//        return Map.of(
//                "allVendors", all,
//                "topVendors", all.stream().limit(3).toList()
//        );
//    }
//}