package com.abel.sistema_gestion.controller;

import com.abel.sistema_gestion.dto.paymentLinks.PaymentLinkResponse;
import com.abel.sistema_gestion.serviceimpl.service.PaymentLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/paymentLinks")
@RequiredArgsConstructor
@CrossOrigin(value = "*")
public class PaymentLinksController {
    private final PaymentLinkService paymentLinkService;

    @PreAuthorize("hasAnyAuthority('PROPIETARIO')")
    @GetMapping("/{userId}")
    public ResponseEntity<List<PaymentLinkResponse>> getAllPaymentLinksByUserId(@PathVariable Integer userId){
        List<PaymentLinkResponse> responses = paymentLinkService.getAllPaymentLinksByUserId(userId);
        return ResponseEntity.ok(responses);
    }
}
