package com.abel.sistema_gestion.dto.vendorSale;

import com.abel.sistema_gestion.dto.cartItem.CartItemResponse;
import com.abel.sistema_gestion.dto.client.ClientResponse;
import com.abel.sistema_gestion.dto.vendor.VendorResponse;
import com.abel.sistema_gestion.enums.SaleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorSaleResponse {

    private Integer id;
    private LocalDate date;
    private String invoiceNumber;
    private String saleStatus;
    private String companyName;
    private VendorBasicResponse vendorResponse;
    private ClientVendorSaleResponse clientResponse;
    private List<CartItemBasicResponse> cartItemResponseList;
}
