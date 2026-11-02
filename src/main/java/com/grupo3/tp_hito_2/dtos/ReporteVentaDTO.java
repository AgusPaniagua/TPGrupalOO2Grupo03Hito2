package com.grupo3.tp_hito_2.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReporteVentaDTO {

    private Long idFestival;
    private String nombreFestival;
    private double recaudacionTotal;
}