package com.abel.sistema_gestion.dto.vendor;

import com.abel.sistema_gestion.dto.client.ClientResponse;
import com.abel.sistema_gestion.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorResponse {

    private Integer id;

    private Integer userId;

    private String name;

    private String surname;

    private String email;

    private Role role;

    private String vendorStatus;

    private String startOfActivity;

    private List<ClientResponse> clientResponses;
}
