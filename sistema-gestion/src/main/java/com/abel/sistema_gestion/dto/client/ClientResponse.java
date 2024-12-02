package com.abel.sistema_gestion.dto.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponse {

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
    private boolean activo;
}
