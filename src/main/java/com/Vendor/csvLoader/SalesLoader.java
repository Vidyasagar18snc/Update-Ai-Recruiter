//package com.Vendor.csvLoader;
//
//import com.Vendor.entity.Sale;
//import com.Vendor.repository.SalesRepository;
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
//public class SalesLoader {
//
//    private final SalesRepository repository;
//
//    @PostConstruct
//    public void load() {
//
//        List<Sale> salesList = new ArrayList<>();
//
//        try {
//            InputStream is = getClass().getClassLoader()
//                    .getResourceAsStream("sales_data.csv");
//
//            if (is == null) {
//                System.out.println("❌ Sales file not found");
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
//                // ✅ Safe CSV split
//                String[] d = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
//
//                if (d.length < 14) {
//                    System.out.println("⚠ Skipping invalid row: " + line);
//                    continue;
//                }
//
//                Sale s = Sale.builder()
//                        .productId(clean(d[0]))
//                        .saleDate(clean(d[1]))
//                        .salesRep(clean(d[2]))
//                        .region(clean(d[3]))
//                        .salesAmount(parseDouble(d[4]))
//                        .quantitySold(parseInt(d[5]))
//                        .productCategory(clean(d[6]))
//                        .unitCost(parseDouble(d[7]))
//                        .unitPrice(parseDouble(d[8]))
//                        .customerType(clean(d[9]))
//                        .discount(parseDouble(d[10]))
//                        .paymentMethod(clean(d[11]))
//                        .salesChannel(clean(d[12]))
//                        .regionAndSalesRep(clean(d[13]))
//                        .build();
//
//                salesList.add(s);
//            }
//
//            // ✅ Save to MongoDB
//            repository.saveAll(salesList);
//
//            System.out.println("✅ Sales Loaded into DB: " + salesList.size());
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