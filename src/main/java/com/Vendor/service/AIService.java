//package com.Vendor.service;
//
//import com.Vendor.entity.*;
//import com.Vendor.repository.*;
//import dev.langchain4j.model.ollama.OllamaChatModel;
//import lombok.RequiredArgsConstructor;
//
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.*;
//
//@Service
//@RequiredArgsConstructor
//public class AIService {
//
//    private final OllamaChatModel model;
//    private final ProcurementRepository procurementRepo;
//    private final SalesRepository salesRepo;
//    private final SupplyChainRepository supplyRepo;
//
//    // ================== 🤖 CHAT ==================
//    public String ask(String question) {
//
//        String context = buildSalesContext()
//                + "\n" + buildProcurementContext()
//                + "\n" + buildSupplyContext();
//
//        String prompt = """
//        You are a smart business analyst AI.
//        Answer in 1-2 lines only.
//
//        DATA:
//        %s
//
//        QUESTION:
//        %s
//        """.formatted(context, question);
//
//        return model.generate(prompt);
//    }
//
//    // ================== 📊 DB ANALYSIS ==================
//    public List<VendorAnalysisDTO> getTopVendors() {
//
//        List<ProcurementOrder> procurement = procurementRepo.findAll();
//        List<Supply> supply = supplyRepo.findAll();
//
//        Map<String, VendorAnalysisDTO> vendorMap = new HashMap<>();
//
//        // 🔥 PROCUREMENT
//        for (ProcurementOrder p : procurement) {
//
//            String supplier = p.getSupplier();
//
//            VendorAnalysisDTO v = vendorMap.getOrDefault(supplier, new VendorAnalysisDTO());
//            v.setSupplier(supplier);
//
//            v.setTotalDefects(v.getTotalDefects() + p.getDefectiveUnits());
//            v.setTotalQuantity(v.getTotalQuantity() + p.getQuantity());
//
//            v.setTotalPrice(v.getTotalPrice() + p.getNegotiatedPrice());
//            v.setPriceCount(v.getPriceCount() + 1);
//
//            v.setCompliance(
//                    v.getCompliance() == 0 ? p.getCompliance()
//                            : (v.getCompliance() + p.getCompliance()) / 2
//            );
//
//            vendorMap.put(supplier, v);
//        }
//
//        // 🔥 SUPPLY
//        for (Supply s : supply) {
//
//            String supplier = s.getSupplierName();
//
//            VendorAnalysisDTO v = vendorMap.get(supplier);
//            if (v == null) continue;
//
//            v.setTotalDeliveryDays(v.getTotalDeliveryDays() + s.getLeadTime());
//            v.setDeliveryCount(v.getDeliveryCount() + 1);
//        }
//
//        return calculateFinalResults(vendorMap);
//    }
//
//    // ================== 📁 FILE ANALYSIS ==================
//    public List<VendorAnalysisDTO> analyzeFile(MultipartFile file) {
//
//        Map<String, VendorAnalysisDTO> vendorMap = new HashMap<>();
//
//        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
//
//            Sheet sheet = workbook.getSheetAt(0);
//
//            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
//
//                Row row = sheet.getRow(i);
//                if (row == null) continue;
//
//                String supplier = row.getCell(0).getStringCellValue();
//                double price = row.getCell(1).getNumericCellValue();
//                double quantity = row.getCell(2).getNumericCellValue();
//                double defective = row.getCell(3).getNumericCellValue();
//                double compliance = row.getCell(4).getNumericCellValue();
//                double delivery = row.getCell(5).getNumericCellValue();
//
//                VendorAnalysisDTO v = vendorMap.getOrDefault(supplier, new VendorAnalysisDTO());
//                v.setSupplier(supplier);
//
//                v.setTotalPrice(v.getTotalPrice() + price);
//                v.setPriceCount(v.getPriceCount() + 1);
//
//                v.setTotalQuantity(v.getTotalQuantity() + quantity);
//                v.setTotalDefects(v.getTotalDefects() + defective);
//
//                v.setTotalDeliveryDays(v.getTotalDeliveryDays() + delivery);
//                v.setDeliveryCount(v.getDeliveryCount() + 1);
//
//                v.setCompliance(
//                        v.getCompliance() == 0 ? compliance
//                                : (v.getCompliance() + compliance) / 2
//                );
//
//                vendorMap.put(supplier, v);
//            }
//
//        } catch (Exception e) {
//            throw new RuntimeException("Error reading Excel file", e);
//        }
//
//        return calculateFinalResults(vendorMap);
//    }
//
//    // ================== 🔥 COMMON LOGIC ==================
//    private List<VendorAnalysisDTO> calculateFinalResults(Map<String, VendorAnalysisDTO> vendorMap) {
//
//        for (VendorAnalysisDTO v : vendorMap.values()) {
//
//            double avgDelivery = v.getDeliveryCount() == 0 ? 0 :
//                    v.getTotalDeliveryDays() / v.getDeliveryCount();
//            v.setAvgDeliveryDays(avgDelivery);
//
//            double avgPrice = v.getPriceCount() == 0 ? 0 :
//                    v.getTotalPrice() / v.getPriceCount();
//            v.setBestPrice(avgPrice);
//
//            double defectRate = v.getTotalQuantity() == 0 ? 0 :
//                    (v.getTotalDefects() / v.getTotalQuantity()) * 100;
//            v.setDefectRate(defectRate);
//
//            double score =
//                    (0.3 * (100 - avgDelivery)) +
//                            (0.3 * (100 - avgPrice)) +
//                            (0.2 * (100 - defectRate)) +
//                            (0.2 * v.getCompliance());
//
//            v.setScore(score);
//        }
//
//        List<VendorAnalysisDTO> sorted = vendorMap.values()
//                .stream()
//                .sorted((a, b) -> Double.compare(b.getScore(), a.getScore()))
//                .toList();
//
//        int rank = 1;
//        for (VendorAnalysisDTO v : sorted) {
//            v.setRank(rank++);
//        }
//
//        return sorted;
//    }
//
//    // ================== CONTEXT BUILDERS ==================
//    private String buildProcurementContext() {
//        return "Procurement data available";
//    }
//
//    private String buildSalesContext() {
//        return "Sales data available";
//    }
//
//    private String buildSupplyContext() {
//        return "Supply data available";
//    }
//}