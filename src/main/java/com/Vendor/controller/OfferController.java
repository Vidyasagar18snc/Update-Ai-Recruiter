package com.Vendor.controller;

import com.Vendor.model.OfferRequestDTO;
import com.Vendor.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/offer")
@RequiredArgsConstructor
@CrossOrigin
public class OfferController {

    private final OfferService offerService;

    @PostMapping("/send")
    public String sendOffer(@RequestBody OfferRequestDTO dto) {
        return offerService.sendOffer(dto);
    }

}