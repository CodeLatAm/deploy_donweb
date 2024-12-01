package com.abel.sistema_gestion.serviceimpl;

import com.abel.sistema_gestion.dto.paymentLinks.PaymentLinkResponse;
import com.abel.sistema_gestion.entity.PaymentLink;
import com.abel.sistema_gestion.mapper.PaymentLinkMapper;
import com.abel.sistema_gestion.repository.PaymentLinkRepository;
import com.abel.sistema_gestion.serviceimpl.service.PaymentLinkService;
import com.abel.sistema_gestion.serviceimpl.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentLinksServiceImpl implements PaymentLinkService {

    private final PaymentLinkRepository paymentLinkRepository;
    private final PaymentLinkMapper paymentLinkMapper;
    private final UserService userService;

    @Override
    public List<PaymentLinkResponse> getAllPaymentLinksByUserId(Integer userId) {
        userService.getUserByUserId(userId);
        List<PaymentLink> paymentLinks = paymentLinkRepository.findAllByUserIdOrderByCreatedDateDesc(userId);
        return paymentLinkMapper.mapToPaymentLinks(paymentLinks);
    }
}
