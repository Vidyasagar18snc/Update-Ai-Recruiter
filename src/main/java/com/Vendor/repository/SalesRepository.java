//package com.Vendor.repository;
//
//import com.Vendor.entity.Sale;
//import org.springframework.data.mongodb.repository.MongoRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface SalesRepository extends MongoRepository<Sale, String> {
//    List<Sale> findTop10ByOrderBySaleDateDesc();
//    List<Sale> findTop5ByOrderBySalesAmountDesc();
//    List<Sale> findTop5ByOrderBySalesAmountAsc();
//    List<Sale> findTop5ByRegion(String region);
//}