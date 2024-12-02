package com.abel.sistema_gestion.dto.vendorSale;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorBasicResponse {
    private Integer id;

    private String name;

    private String surname;
}
