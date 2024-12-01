package com.abel.sistema_gestion.controller;

import com.abel.sistema_gestion.dto.MessageResponse;
import com.abel.sistema_gestion.dto.freePrice.FeePriceRequest;
import com.abel.sistema_gestion.dto.freePrice.FeePriceResponse;
import com.abel.sistema_gestion.serviceimpl.service.FeePriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/prices")
@RequiredArgsConstructor
@CrossOrigin(value = "*")
public class FeePriceController {
    private final FeePriceService feePriceService;

    @PostMapping("/create")
    public ResponseEntity<MessageResponse> createFeePrice(@RequestBody FeePriceRequest request){
        return ResponseEntity.ok(feePriceService.createFeePrice(request));
    }

    @GetMapping
    public ResponseEntity<List<FeePriceResponse>> getAllFeePrice(){
        List<FeePriceResponse> responses = feePriceService.getAllFeePrice();
        return ResponseEntity.ok(responses);
    }
}
