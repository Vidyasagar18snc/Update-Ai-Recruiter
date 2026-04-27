//package com.Vendor.util;
//
//import com.Vendor.service.OCRService;
//import lombok.AllArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//@Service @AllArgsConstructor
//public class DocumentProcessingService {
//
//
//    private  final OCRService ocrService;
//
//
//    private final FileStorageService fileStorageService;
//
//    public String process(MultipartFile file) {
//
//        // ✅ Step 1: Store file in MongoDB
//        String fileId = fileStorageService.storeFile(file);
//
//        // ✅ Step 2: Scan using OCR
//        String extractedText = ocrService.scanDocument(file);
//
//        return "File Stored with ID: " + fileId + "\n\nOCR Result:\n" + extractedText;
//    }
//}