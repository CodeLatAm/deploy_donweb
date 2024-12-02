package com.abel.sistema_gestion.dto.afip;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacturaRequest {
    private String tipoComprobante;
    private String numeroComprobante;
    private String fecha;
    private String tipoDocumento;
    private String numeroDocumento;
    private double importeTotal;
}
