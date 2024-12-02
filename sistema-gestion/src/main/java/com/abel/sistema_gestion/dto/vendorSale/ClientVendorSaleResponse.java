package com.abel.sistema_gestion.dto.vendorSale;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientVendorSaleResponse {
    private Integer id;
    private String name;
    private String address;
    private String phone;
    private String cp;
    private String email;
    private String betweenStreets;
    private String location;
    private Integer VendorId;
    private String province;
    private String dni;
}
