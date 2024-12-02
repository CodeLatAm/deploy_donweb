package com.abel.sistema_gestion.mapper;

import com.abel.sistema_gestion.dto.vendorSale.VendorSaleResponse;
import com.abel.sistema_gestion.entity.VendorSales;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class VendorSalesMapper {
    private final ClientMapper clientMapper;
    private final VendorMapper vendorMapper;
    private final CartItemMapper cartItemMapper;

    public List<VendorSaleResponse> mapToVendorSaleList(List<VendorSales> vendorSales, String companyName) {
        return vendorSales.stream()
                .map(vendorSale -> mapToVendorSaleResponse(vendorSale, companyName)) // Pasamos companyName
                .collect(Collectors.toList());
    }

    private VendorSaleResponse mapToVendorSaleResponse(VendorSales vendorSales, String companyName) {
        String vendorSalesStatus = vendorSales.getSaleStatus().name();

        return VendorSaleResponse
                .builder()
                .id(vendorSales.getId())
                .date(vendorSales.getDate())
                .invoiceNumber(vendorSales.getInvoiceNumber())
                .saleStatus(vendorSalesStatus)
                .vendorResponse(vendorMapper.mapToVendorBasicResponse(vendorSales.getVendor()))
                .clientResponse(clientMapper.mapToClientVendorSaleResponse(vendorSales.getClient()))
                .cartItemResponseList(cartItemMapper.mapToCartItemBasicResponse(vendorSales.getCartItemVendorSales()))
                .companyName(companyName) // Seteamos el nombre de la compañía
                .build();
    }

    public Page<VendorSaleResponse> mapToVendorSalesPage(Page<VendorSales> salesPage, String companyName) {
        // Usamos una lambda para pasar el companyName al método convertToVendorSaleResponse
        return salesPage.map(vendorSales -> convertToVendorSaleResponse(vendorSales, companyName));
    }

    // Modificamos el método convertToVendorSaleResponse para recibir el companyName como parámetro
    private VendorSaleResponse convertToVendorSaleResponse(VendorSales vendorSales, String companyName) {
        String vendorSalesStatus = vendorSales.getSaleStatus().name();

        return VendorSaleResponse
                .builder()
                .id(vendorSales.getId())
                .date(vendorSales.getDate())
                .invoiceNumber(vendorSales.getInvoiceNumber())
                .saleStatus(vendorSalesStatus)
                .vendorResponse(vendorMapper.mapToVendorBasicResponse(vendorSales.getVendor()))
                .clientResponse(clientMapper.mapToClientVendorSaleResponse(vendorSales.getClient()))
                .cartItemResponseList(cartItemMapper.mapToCartItemBasicResponse(vendorSales.getCartItemVendorSales()))
                .companyName(companyName) // Seteamos el nombre de la compañía
                .build();
    }

}
