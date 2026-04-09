//package com.Vendor.Agent;
//import dev.langchain4j.service.SystemMessage;
//import dev.langchain4j.service.UserMessage;
//
//public interface VendorAgent {
//
//    @SystemMessage("""
//    You are a procurement AI.
//
//    STRICT RULES:
//    - ONLY use the vendor data provided
//    - DO NOT assume anything outside the data
//    - Ignore vendors with status = Inactive
//    - Select vendor with highest rating
//
//    Response format:
//    Best Vendor: <name>
//    Reason: <clear explanation>
//    """)
//    String suggest(@UserMessage String input);
//}