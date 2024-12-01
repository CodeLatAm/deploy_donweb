package com.abel.sistema_gestion.serviceimpl.service;

import com.abel.sistema_gestion.dto.MessageResponse;
import com.abel.sistema_gestion.dto.vendorSale.ClientVendorSaleResponse;
import com.abel.sistema_gestion.dto.vendorSale.PaymentTypeSalesResponse;
import com.abel.sistema_gestion.dto.vendorSale.VendorSaleResponse;
import com.abel.sistema_gestion.dto.vendorSale.VendorSalesRequest;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface VendorSaleService {
    MessageResponse createVendorSale(Integer cartId, Integer clientId, VendorSalesRequest request);

    List<VendorSaleResponse> getVendorSale(Integer vendorId);

    MessageResponse updateProductStatus(Integer id);

    Page<VendorSaleResponse> getVendorSalePage(Integer vendorId, int page, int size);

    Page<VendorSaleResponse> getFilteredVendorSalePage(Integer vendorId, int page, int size, String search, Integer dateFilter);

    Map<String, Long> getSalesCountByVendorId(Integer vendorId);

    Map<String, Long> getSalesCountByShippingArea(Integer vendorId);

    Long getSalesCount(Integer vendorId);

    Map<String, Long> getSalesCountByYearAndUserId(Integer userId);

    Map<String, Long> getSalesCountByShippingAreaAndUserId(Integer userId);

    Long getCountSalesByUserId(Integer userId);

    List<ClientVendorSaleResponse> getSuggestedVendorSales(Integer vendorId, String search);

    Page<VendorSaleResponse> getVendorsSalesPageFilter(Integer vendorId, int page, int size, String search);

    Page<VendorSaleResponse> getVendorSalesByFilterDate(Integer vendorId, Integer year, LocalDate startDate, LocalDate endDate, int page, int size);

    List<PaymentTypeSalesResponse> getSalesCountByPaymentType(Integer userId);
}
