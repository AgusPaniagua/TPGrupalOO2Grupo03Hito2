package com.grupo3.tp_hito_2.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Liquidación de haberes de todo el personal de una unidad en su festival (no persiste).
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LiquidacionUnidadDTO {
    private String nombreUnidad;
    private String nombreFestival;
    private long diasFestival;
    private List<ReciboSueldoDTO> recibos;
    private double totalAPagar;
}
