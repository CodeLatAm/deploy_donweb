package com.abel.sistema_gestion.serviceimpl.service;

import com.abel.sistema_gestion.dto.MessageResponse;
import com.abel.sistema_gestion.dto.freePrice.FeePriceRequest;
import com.abel.sistema_gestion.dto.freePrice.FeePriceResponse;

import java.util.List;

public interface FeePriceService {
    MessageResponse createFeePrice(FeePriceRequest request);


    List<FeePriceResponse> getAllFeePrice();
}
