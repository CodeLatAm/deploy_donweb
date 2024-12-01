package com.abel.sistema_gestion.entity;

import com.abel.sistema_gestion.enums.LinkStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payment_links")
public class PaymentLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String paymentUrl;
    private Long userId;
    private LocalDateTime createdDate;
    private boolean paid; // Estado del pago
    private String paymentId;
    @Enumerated(EnumType.STRING)
    private LinkStatus linkStatus;
}
