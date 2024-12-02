package com.abel.sistema_gestion.repository;

import com.abel.sistema_gestion.entity.PaymentLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentLinkRepository extends JpaRepository<PaymentLink, Long> {
    Optional<PaymentLink> findByPaymentId(String paymentId);

    List<PaymentLink> findAllByUserIdOrderByCreatedDateDesc(Integer userId);
}
