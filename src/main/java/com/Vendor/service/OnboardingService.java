//package com.Vendor.service;
//
//import com.Vendor.dto.OCRResult;
//import com.Vendor.model.Candidate;
//import com.Vendor.model.Documents;
//import com.Vendor.repository.CandidateRepository;
//import com.Vendor.repository.DocumentRepository;
//import com.Vendor.util.EmployeeUtil;
//import lombok.AllArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.List;
//
//@Service @AllArgsConstructor
//public class OnboardingService {
//
//    private  final OCRService ocrService;
//    private  final DocumentRepository documentRepository;
//    private  final CandidateRepository candidateRepository;
//    private  final EmailService emailService;
//
//    // ================= UPLOAD + OCR + SAVE =================
//    public void uploadDocument(String candidateId, MultipartFile file) {
//
//        OCRResult result = ocrService.scanDocument(file);
//
//        Documents doc = new Documents();
//        doc.setCandidateId(candidateId);
//        doc.setDocumentType(result.getDocumentType());
//        doc.setStatus(result.getStatus());
//        doc.setPan(result.getPan());
//        doc.setAadhaar(result.getAadhaar());
//        doc.setAccountNumber(result.getAccountNumber());
//
//        documentRepository.save(doc);
//
//        checkAndCompleteOnboarding(candidateId);
//    }
//
//    // ================= CHECK ALL DOCS =================
//    private void checkAndCompleteOnboarding(String candidateId) {
//
//        List<Documents> docs = documentRepository.findByCandidateId(candidateId);
//
//        boolean allVerified = docs.stream()
//                .allMatch(d -> "VERIFIED".equalsIgnoreCase(d.getStatus()));
//
//        if (!allVerified) return;
//
//        Candidate candidate = candidateRepository.findById(candidateId).orElseThrow();
//
//        if (candidate.isDocumentsVerified()) return;
//
//        candidate.setDocumentsVerified(true);
//
//        // 🔥 GENERATE EMPLOYEE DETAILS
//        String empId = EmployeeUtil.generateEmployeeId();
//        String email = EmployeeUtil.generateCompanyEmail(candidate.getName());
//
//        candidate.setEmployeeId(empId);
//        candidate.setCompanyEmail(email);
//
//        candidateRepository.save(candidate);
//
//        sendWelcomeMail(candidate);
//    }
//
//    // ================= EMAIL =================
//    private void sendWelcomeMail(Candidate candidate) {
//
//        String msg = "Hi " + candidate.getName() + ",\n\n"
//                + "🎉 Your documents are verified successfully.\n\n"
//                + "Employee ID: " + candidate.getEmployeeId() + "\n"
//                + "Company Email: " + candidate.getCompanyEmail() + "\n\n"
//                + "Welcome to the company!\n\n"
//                + "HR Team";
//
//        emailService.sendOnboardingSuccessEmail(
//                candidate.getEmail(),
//                candidate.getName(),
//                candidate.getEmployeeId(),
//                candidate.getCompanyEmail()
//        );
//    }
//}