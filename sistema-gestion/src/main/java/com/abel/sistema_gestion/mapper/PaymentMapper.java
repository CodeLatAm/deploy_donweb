package com.abel.sistema_gestion.mapper;

import com.abel.sistema_gestion.dto.payment.PaymentResponse;
import com.abel.sistema_gestion.dto.payment.PaymentResponseFull;
import com.abel.sistema_gestion.entity.Payment;
import com.abel.sistema_gestion.serviceimpl.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PaymentMapper {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final UserService userService;
    private final UserMapper userMapper;

    public List<PaymentResponse> mapToPayments(List<Payment> payments) {
        return payments.stream().map(this::mapToPaymentsResponse).collect(Collectors.toList());
    }

    private PaymentResponse mapToPaymentsResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .paymentId(payment.getPaymentId())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .dateCreated(formatDate(payment.getDateCreated()))
                .description(payment.getDescription())
                .status(payment.getStatus())

                .build();
    }

    private String formatDate(OffsetDateTime date) {
        return date != null ? date.format(FORMATTER) : null;
    }

    public List<PaymentResponseFull> mapToPaymentsList(List<Payment> payments) {

        return payments.stream().map(this::mapToPaymentsResponseFull).collect(Collectors.toList());
    }

    private PaymentResponseFull mapToPaymentsResponseFull(Payment payment) {
        return PaymentResponseFull.builder()
                .id(payment.getId())
                .paymentId(payment.getPaymentId())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .dateCreated(formatDate(payment.getDateCreated()))
                .description(payment.getDescription())
                .status(payment.getStatus())
                .userBasicInfoResponse(userMapper.mapToBasicUserResponse(userService.getUserByUserId(payment.getUserId())))
                .build();
    }
}
