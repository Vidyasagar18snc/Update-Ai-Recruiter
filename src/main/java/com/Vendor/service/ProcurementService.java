//package com.Vendor.service;
//
//import com.Vendor.entity.ProcurementOrder;
//import com.Vendor.repository.ProcurementRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class ProcurementService {
//
//    private final ProcurementRepository repository;
//
//    public String getContext() {
//
//        List<ProcurementOrder> list = repository.findTop10ByOrderByOrderDateDesc();
//
//        StringBuilder sb = new StringBuilder("Procurement Data:\n");
//
//        for (ProcurementOrder p : list) {
//            sb.append(String.format(
//                    "Supplier: %s, Status: %s, Defects: %d, Compliance: %s%n",
//                    p.getSupplier(),
//                    p.getOrderStatus(),
//                    p.getDefectiveUnits(),
//                    p.getCompliance()
//            ));
//        }
//
//        return sb.toString();
//    }
//}