//package com.Vendor.service;
//
//import com.Vendor.entity.Sale;
//import com.Vendor.repository.SalesRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class SalesService {
//
//    private final SalesRepository repository;
//
//    public String getContext() {
//
//        List<Sale> list = repository.findTop10ByOrderBySaleDateDesc();
//
//        StringBuilder sb = new StringBuilder("Sales Data:\n");
//
//        for (Sale s : list) {
//            sb.append(String.format(
//                    "Region: %s, SalesRep: %s, Amount: %.2f%n",
//                    s.getRegion(),
//                    s.getSalesRep(),
//                    s.getSalesAmount()
//            ));
//        }
//
//        return sb.toString();
//    }
//}