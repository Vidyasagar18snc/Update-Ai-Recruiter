//package com.Vendor.repository;
//
//import com.Vendor.entity.ProcurementOrder;
//import org.springframework.data.mongodb.repository.MongoRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface ProcurementRepository extends MongoRepository<ProcurementOrder, String> {
//    List<ProcurementOrder> findTop10ByOrderByOrderDateDesc();
//    List<ProcurementOrder> findTop5ByOrderByDefectiveUnitsDesc();
//    List<ProcurementOrder> findTop5ByOrderByComplianceDesc();
//}