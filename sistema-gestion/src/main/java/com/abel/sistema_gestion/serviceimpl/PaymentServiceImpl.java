package com.abel.sistema_gestion.serviceimpl;

import com.abel.sistema_gestion.dto.payment.PaymentResponse;
import com.abel.sistema_gestion.dto.payment.PaymentResponseFull;
import com.abel.sistema_gestion.entity.Payment;
import com.abel.sistema_gestion.entity.User;
import com.abel.sistema_gestion.exception.UserNotFoundException;
import com.abel.sistema_gestion.mapper.PaymentMapper;
import com.abel.sistema_gestion.repository.PaymentRepository;
import com.abel.sistema_gestion.serviceimpl.service.PaymentService;
import com.abel.sistema_gestion.serviceimpl.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final UserService userService;

    @Override
    public void save(Payment payment) {
        paymentRepository.save(payment);

    }

    @Override
    public List<PaymentResponse> getAllPaymentsByUserId(Integer userId) {
        userService.getUserByUserId(userId);
        List<Payment> payments = paymentRepository.findAllByUserIdOrderByDateCreatedDesc(userId);
        return paymentMapper.mapToPayments(payments);
    }

    @Override
    public List<PaymentResponseFull> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        return paymentMapper.mapToPaymentsList(payments);
    }

    @Override
    public Map<Integer, BigDecimal> getEarningsByYear() {
        List<Object[]> results = paymentRepository.calculateEarningsByYear();
        Map<Integer, BigDecimal> earningsByYear = new LinkedHashMap<>();
        for (Object[] result : results) {
            Integer year = (Integer) result[0];
            BigDecimal total = (BigDecimal) result[1];
            earningsByYear.put(year, total);
        }
        return earningsByYear;
    }
}
