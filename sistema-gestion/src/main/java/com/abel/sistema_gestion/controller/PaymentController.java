package com.abel.sistema_gestion.controller;

import com.abel.sistema_gestion.dto.payment.PaymentResponse;
import com.abel.sistema_gestion.dto.payment.PaymentResponseFull;
import com.abel.sistema_gestion.serviceimpl.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@CrossOrigin(value = "*")
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<PaymentResponse>> getAllPaymentsByUserId(@PathVariable Integer userId){
        List<PaymentResponse> responses = paymentService.getAllPaymentsByUserId(userId);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<PaymentResponseFull>> getAllPayments () {
        List<PaymentResponseFull> responses = paymentService.getAllPayments();
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/earnings-by-year")
    public ResponseEntity<Map<Integer, BigDecimal>> getEarningsByYear() {
        Map<Integer, BigDecimal> earningsByYear = paymentService.getEarningsByYear();
        return ResponseEntity.ok(earningsByYear);
    }
}
