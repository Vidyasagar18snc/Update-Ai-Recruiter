//package com.Vendor.service;
//
//import com.Vendor.entity.Supply;
//import com.Vendor.repository.SupplyChainRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class SupplyChainService {
//
//    private final SupplyChainRepository repository;
//
//    public String getContext() {
//
//        List<Supply> list = repository.findTop10ByOrderByLeadTimeAsc();
//
//        StringBuilder sb = new StringBuilder("Supply Chain Data:\n");
//
//        for (Supply s : list) {
//            sb.append(String.format(
//                    "Supplier: %s, Price: %.2f, LeadTime: %d, ShippingCost: %.2f%n",
//                    s.getSupplierName(),
//                    s.getPrice(),
//                    s.getLeadTime(),
//                    s.getShippingCosts()
//            ));
//        }
//
//        return sb.toString();
//    }
//}