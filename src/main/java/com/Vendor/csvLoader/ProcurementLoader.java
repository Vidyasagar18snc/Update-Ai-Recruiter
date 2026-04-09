//package com.Vendor.csvLoader;
//
//import com.Vendor.entity.ProcurementOrder;
//import com.Vendor.repository.ProcurementRepository;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//import java.io.BufferedReader;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//public class ProcurementLoader {
//
//    private final ProcurementRepository repository;
//
//    @PostConstruct
//    public void load() {
//
//        List<ProcurementOrder> orders = new ArrayList<>();
//
//        try {
//            InputStream is = getClass().getClassLoader()
//                    .getResourceAsStream("Procurement KPI Analysis Dataset.csv");
//
//            if (is == null) {
//                System.out.println("❌ Procurement file not found");
//                return;
//            }
//
//            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//
//            String line;
//            reader.readLine(); // skip header
//
//            while ((line = reader.readLine()) != null) {
//
//                // ✅ Safe split (handles commas inside quotes)
//                String[] d = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
//
//                if (d.length < 11) {
//                    System.out.println("⚠ Skipping invalid row: " + line);
//                    continue;
//                }
//
//                ProcurementOrder o = ProcurementOrder.builder()
//                        .poId(clean(d[0]))
//                        .supplier(clean(d[1]))
//                        .orderDate(clean(d[2]))
//                        .deliveryDate(clean(d[3]))
//                        .itemCategory(clean(d[4]))
//                        .orderStatus(clean(d[5]))
//                        .quantity(safeInt(d[6]))
//                        .unitPrice(safeDouble(d[7]))
//                        .negotiatedPrice(safeDouble(d[8]))
//                        .defectiveUnits(safeInt(d[9]))
//                        .compliance(parseCompliance(d[10]))
//                        .build();
//
//                orders.add(o);
//            }
//
//            // ✅ Save to MongoDB
//            repository.saveAll(orders);
//
//            System.out.println("✅ Procurement loaded into DB: " + orders.size());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    // 🔧 Helpers
//
//    private String clean(String val) {
//        return val == null ? "" : val.replace("\"", "").trim();
//    }
//
//    private double parseCompliance(String val) {
//        if (val == null) return 0.0;
//
//        val = val.trim().toLowerCase();
//
//        if (val.equals("yes")) return 1.0;
//        if (val.equals("no")) return 0.0;
//
//        try {
//            return Double.parseDouble(val);
//        } catch (Exception e) {
//            return 0.0;
//        }
//    }
//
//    private double safeDouble(String val) {
//        if (val == null || val.trim().isEmpty()) return 0.0;
//        try { return Double.parseDouble(val.trim()); }
//        catch (Exception e) { return 0.0; }
//    }
//
//    private int safeInt(String val) {
//        if (val == null || val.trim().isEmpty()) return 0;
//        try { return Integer.parseInt(val.trim()); }
//        catch (Exception e) { return 0; }
//    }
//}