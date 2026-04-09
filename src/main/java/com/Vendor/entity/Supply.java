//package com.Vendor.entity;
//
//import lombok.*;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.Document;
//
//@Document(collection = "supply_chain")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class Supply {
//
//    @Id
//    private String id;
//
//    private String productType;
//    private String sku;
//
//    private Double price;
//    private Integer availability;
//
//    private Integer numberOfProductsSold;
//    private Double revenueGenerated;
//
//    private String customerDemographics;
//
//    private Integer stockLevels;
//    private Integer leadTime; // unified lead time
//
//    private Integer orderQuantities;
//    private Integer shippingTime;
//
//    private String shippingCarriers;
//    private Double shippingCosts;
//
//    private String supplierName;
//    private String location;
//
//    private Integer productionVolumes;
//    private Integer manufacturingLeadTime;
//    private Double manufacturingCosts;
//
//    private String inspectionResults;
//    private Double defectRates;
//
//    private String transportationModes;
//    private String routes;
//
//    private Double totalCosts; // renamed from "Costs"
//}