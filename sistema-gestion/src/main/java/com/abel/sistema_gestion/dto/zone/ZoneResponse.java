package com.abel.sistema_gestion.dto.zone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZoneResponse {

    private Integer id;
    private String cp;
    private String location;
    private Double cost;
    private Integer userId;
    private String province;
}
