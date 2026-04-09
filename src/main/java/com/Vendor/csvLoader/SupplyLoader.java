//package com.Vendor.csvLoader;
//
//import com.Vendor.entity.Supply;
//import com.Vendor.repository.SupplyChainRepository;
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
//public class SupplyLoader {
//
//    private final SupplyChainRepository repository;
//
//    @PostConstruct
//    public void load() {
//
//        List<Supply> supplies = new ArrayList<>();
//
//        try {
//            InputStream is = getClass().getClassLoader()
//                    .getResourceAsStream("supply_chain_data.csv");
//
//            if (is == null) {
//                System.out.println("❌ Supply file not found");
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
//                String[] d = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
//
//                if (d.length < 24) {
//                    System.out.println("⚠ Skipping invalid row: " + line);
//                    continue;
//                }
//
//                Supply s = Supply.builder()
//                        .productType(clean(d[0]))
//                        .sku(clean(d[1]))
//                        .price(parseDouble(d[2]))
//                        .availability(parseInt(d[3]))
//                        .numberOfProductsSold(parseInt(d[4]))
//                        .revenueGenerated(parseDouble(d[5]))
//                        .customerDemographics(clean(d[6]))
//                        .stockLevels(parseInt(d[7]))
//                        .leadTime(parseInt(d[8])) // choose one lead time
//                        .orderQuantities(parseInt(d[9]))
//                        .shippingTime(parseInt(d[10]))
//                        .shippingCarriers(clean(d[11]))
//                        .shippingCosts(parseDouble(d[12]))
//                        .supplierName(clean(d[13]))
//                        .location(clean(d[14]))
//                        .productionVolumes(parseInt(d[16]))
//                        .manufacturingLeadTime(parseInt(d[17]))
//                        .manufacturingCosts(parseDouble(d[18]))
//                        .inspectionResults(clean(d[19]))
//                        .defectRates(parseDouble(d[20]))
//                        .transportationModes(clean(d[21]))
//                        .routes(clean(d[22]))
//                        .totalCosts(parseDouble(d[23]))
//                        .build();
//
//                supplies.add(s);
//            }
//
//            // ✅ Save to MongoDB
//            repository.saveAll(supplies);
//
//            System.out.println("✅ Supply Loaded into DB: " + supplies.size());
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
//    private double parseDouble(String val) {
//        try { return Double.parseDouble(val.trim()); }
//        catch (Exception e) { return 0.0; }
//    }
//
//    private int parseInt(String val) {
//        try { return Integer.parseInt(val.trim()); }
//        catch (Exception e) { return 0; }
//    }
//}