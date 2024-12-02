package com.abel.sistema_gestion.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_id", unique = true, nullable = false)
    private String paymentId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false, length = 10)
    private String currency;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "date_created", nullable = false)
    private OffsetDateTime dateCreated;

    @Column(name = "date_approved")
    private OffsetDateTime dateApproved;

    @Column(name = "date_last_updated")
    private OffsetDateTime dateLastUpdated;

    @Column(length = 500)
    private String description;

    @Column(name = "subscription", nullable = false)
    private Boolean subscription;

    @Column(name = "user_id")
    private Integer userId;  // ID del usuario asociado al pago
}
