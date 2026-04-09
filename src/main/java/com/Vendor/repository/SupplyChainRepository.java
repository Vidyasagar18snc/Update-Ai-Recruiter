//package com.Vendor.repository;
//
//import com.Vendor.entity.Supply;
//import org.springframework.data.mongodb.repository.MongoRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface SupplyChainRepository extends MongoRepository<Supply, String> {
//    List<Supply> findTop10ByOrderByLeadTimeAsc();
//    List<Supply> findTop5ByOrderByLeadTimeAsc();
//    List<Supply> findTop5ByOrderByShippingCostsDesc();
//}