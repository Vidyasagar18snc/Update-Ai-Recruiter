//package com.Vendor.util;
//
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.text.PDFTextStripper;
//import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
//import org.apache.poi.xwpf.usermodel.XWPFDocument;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//@Service
//public class FileExtractionService {
//
//    public String extractText(MultipartFile file) {
//        try {
//            String fileName = file.getOriginalFilename();
//
//            if (fileName.endsWith(".pdf")) {
//                return extractFromPDF(file);
//            } else if (fileName.endsWith(".docx")) {
//                return extractFromDocx(file);
//            } else {
//                throw new RuntimeException("Unsupported file format");
//            }
//
//        } catch (Exception e) {
//            throw new RuntimeException("Error extracting file content");
//        }
//    }
//
//    private String extractFromPDF(MultipartFile file) throws Exception {
//        PDDocument document = PDDocument.load(file.getInputStream());
//        PDFTextStripper stripper = new PDFTextStripper();
//        String text = stripper.getText(document);
//        document.close();
//        return text;
//    }
//
//    private String extractFromDocx(MultipartFile file) throws Exception {
//        XWPFDocument doc = new XWPFDocument(file.getInputStream());
//        XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
//        return extractor.getText();
//    }
//}