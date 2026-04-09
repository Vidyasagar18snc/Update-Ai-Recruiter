//package com.Vendor.config;
//
//import dev.langchain4j.model.ollama.OllamaChatModel;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class OllamaConfig {
//
//    @Bean
//    public OllamaChatModel ollamaChatModel() {
//        return OllamaChatModel.builder()
//                .baseUrl("http://localhost:11434")
//                .modelName("tinyllama") // 🔥 LOW RAM MODEL
//                .temperature(0.3)
//                .build();
//    }
//}