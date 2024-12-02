package com.abel.sistema_gestion.mapper;

import com.abel.sistema_gestion.dto.paymentLinks.PaymentLinkResponse;
import com.abel.sistema_gestion.entity.PaymentLink;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PaymentLinkMapper {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public List<PaymentLinkResponse> mapToPaymentLinks(List<PaymentLink> paymentLinks) {
        return paymentLinks.stream().map(this::mapToPaymentLinksResponse).collect(Collectors.toList());
    }

    private PaymentLinkResponse mapToPaymentLinksResponse(PaymentLink paymentLink) {
        return PaymentLinkResponse.builder()
                .id(paymentLink.getId())
                .paymentId(paymentLink.getPaymentId())
                .paymentUrl(paymentLink.getPaymentUrl())
                .createdDate(formatDate(paymentLink.getCreatedDate()))
                .userId(paymentLink.getUserId())
                .linkStatus(paymentLink.getLinkStatus().name())
                .build();
    }

    private String formatDate(LocalDateTime date) {
        return date != null ? date.format(FORMATTER) : null;
    }
}
