//package com.Vendor.service;
//
//import com.Vendor.entity.Vendor;
//import com.Vendor.repository.VendorRepository;
//import org.apache.poi.ss.usermodel.*;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.*;
//
//@Service
//public class VendorService {
//
//    private final VendorRepository vendorRepository;
//
//    public VendorService(VendorRepository vendorRepository) {
//        this.vendorRepository = vendorRepository;
//    }
//
//    // ✅ Upload + Process Excel
//    public Map<String, Object> processExcel(MultipartFile file) throws Exception {
//
//        List<Vendor> vendors = new ArrayList<>();
//
//        Workbook workbook = WorkbookFactory.create(file.getInputStream());
//        Sheet sheet = workbook.getSheetAt(0);
//
//        // ✅ Read Excel
//        for (Row row : sheet) {
//            if (row.getRowNum() == 0) continue;
//
//            try {
//                Vendor v = new Vendor();
//
//                String name = getCellValue(row.getCell(0));
//                String priceStr = getCellValue(row.getCell(1));
//                String deliveryStr = getCellValue(row.getCell(5));
//                String qualityStr = getCellValue(row.getCell(4));
//
//                if (name.isEmpty()) continue;
//
//                v.setName(name);
//                v.setPrice(priceStr.isEmpty() ? 0 : Double.parseDouble(priceStr));
//                v.setDeliveryDays(deliveryStr.isEmpty() ? 0 : (int) Double.parseDouble(deliveryStr));
//                v.setQualityScore(qualityStr.isEmpty() ? 0 : (int) Double.parseDouble(qualityStr));
//
//                vendors.add(v);
//
//            } catch (Exception e) {
//                System.out.println("❌ Error in row: " + row.getRowNum());
//                e.printStackTrace();
//            }
//        }
//
//        if (vendors.isEmpty()) {
//            throw new RuntimeException("Excel file is empty or invalid data");
//        }
//
//        // ✅ Save to DB
//        vendorRepository.saveAll(vendors);
//
//        // =====================================================
//        // 🔥 SMART SCORING LOGIC (Price + Delivery + Quality)
//        // =====================================================
//
//        for (Vendor v : vendors) {
//
//            double score =
//                    (v.getPrice() * 0.4) +          // Price weight
//                            (v.getDeliveryDays() * 0.3) +   // Delivery weight
//                            (v.getQualityScore() * 0.3);    // Quality weight
//
//            v.setScore(score);
//        }
//
//        // ✅ Sort by Score DESC
//        vendors.sort((v1, v2) -> Double.compare(v2.getScore(), v1.getScore()));
//
//        // =====================================================
//        // ✅ Ranking + Split
//        // =====================================================
//
//        List<Map<String, Object>> rankedVendors = new ArrayList<>();
//
//        int rank = 1;
//        for (Vendor v : vendors) {
//
//            Map<String, Object> map = new HashMap<>();
//            map.put("rank", rank++);
//            map.put("name", v.getName());
//            map.put("price", v.getPrice());
//            map.put("delivery", v.getDeliveryDays());
//            map.put("rating", v.getQualityScore());
//            map.put("score", v.getScore());
//
//            rankedVendors.add(map);
//        }
//
//        // ✅ Top 3 Vendors
//        List<Map<String, Object>> topVendors = rankedVendors.stream()
//                .limit(3)
//                .toList();
//
//        // ✅ Remaining Vendors
//        List<Map<String, Object>> remainingVendors = rankedVendors.stream()
//                .skip(3)
//                .toList();
//
//        // =====================================================
//        // ✅ FINAL RESPONSE
//        // =====================================================
//
//        Map<String, Object> result = new HashMap<>();
//        result.put("topVendors", topVendors);
//        result.put("remainingVendors", remainingVendors);
//        result.put("allVendors", rankedVendors);
//
//        return result;
//    }
//
//    // ✅ Safe Cell Reader
//    private String getCellValue(Cell cell) {
//        if (cell == null) return "";
//
//        switch (cell.getCellType()) {
//            case STRING:
//                return cell.getStringCellValue().trim();
//
//            case NUMERIC:
//                return String.valueOf(cell.getNumericCellValue());
//
//            case BOOLEAN:
//                return String.valueOf(cell.getBooleanCellValue());
//
//            default:
//                return "";
//        }
//    }
//
//    // ✅ Fetch All Vendors
//    public List<Vendor> getAllVendors() {
//        return vendorRepository.findAll();
//    }
//
//    // ✅ Get Ranked Vendors (without upload)
//    public Map<String, Object> getTop3AndRemainingVendors() {
//
//        List<Vendor> vendors = vendorRepository.findAll();
//
//        if (vendors.isEmpty()) {
//            throw new RuntimeException("No vendors found");
//        }
//
//        // ✅ Same scoring logic
//        for (Vendor v : vendors) {
//
//            double score =
//                    (v.getPrice() * 0.4) +
//                            (v.getDeliveryDays() * 0.3) +
//                            (v.getQualityScore() * 0.3);
//
//            v.setScore(score);
//        }
//
//        vendors.sort((v1, v2) -> Double.compare(v2.getScore(), v1.getScore()));
//
//        List<Map<String, Object>> rankedVendors = new ArrayList<>();
//
//        int rank = 1;
//        for (Vendor v : vendors) {
//
//            Map<String, Object> map = new HashMap<>();
//            map.put("rank", rank++);
//            map.put("name", v.getName());
//            map.put("price", v.getPrice());
//            map.put("delivery", v.getDeliveryDays());
//            map.put("rating", v.getQualityScore());
//            map.put("score", v.getScore());
//
//            rankedVendors.add(map);
//        }
//
//        List<Map<String, Object>> topVendors = rankedVendors.stream().limit(3).toList();
//        List<Map<String, Object>> remainingVendors = rankedVendors.stream().skip(3).toList();
//
//        Map<String, Object> result = new HashMap<>();
//        result.put("topVendors", topVendors);
//        result.put("remainingVendors", remainingVendors);
//        result.put("allVendors", rankedVendors);
//
//        return result;
//    }
//}