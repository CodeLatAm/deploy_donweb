package com.abel.sistema_gestion.serviceimpl;

import com.abel.sistema_gestion.dto.MessageResponse;
import com.abel.sistema_gestion.dto.freePrice.FeePriceRequest;
import com.abel.sistema_gestion.dto.freePrice.FeePriceResponse;
import com.abel.sistema_gestion.entity.FeePrice;
import com.abel.sistema_gestion.repository.FeePriceRepository;
import com.abel.sistema_gestion.serviceimpl.service.FeePriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeePriceServiceImpl implements FeePriceService {
    private final FeePriceRepository feePriceRepository;


    @Override
    public MessageResponse createFeePrice(FeePriceRequest request) {
        FeePrice price = this.mapToFeePrice(request);
        feePriceRepository.save(price);
        return new MessageResponse("Precio creado", HttpStatus.OK);
    }

    @Override
    public List<FeePriceResponse> getAllFeePrice() {
        List<FeePrice> feePrices = feePriceRepository.findAll();
        List<FeePriceResponse> responses = this.mapToFeePriceList(feePrices);
        return responses;
    }

    private List<FeePriceResponse> mapToFeePriceList(List<FeePrice> feePrices) {
        return feePrices.stream().map(this::mapToFeePriceResponse).collect(Collectors.toList());
    }

    private FeePriceResponse mapToFeePriceResponse(FeePrice feePrice) {
        return FeePriceResponse.builder()
                .id(feePrice.getId())
                .price(feePrice.getPrice())
                .build();
    }

    private FeePrice mapToFeePrice(FeePriceRequest request) {
        return FeePrice.builder()
                .price(request.getPrice())
                .build();
    }
}
