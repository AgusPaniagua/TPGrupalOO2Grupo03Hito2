package com.grupo3.tp_hito_2.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RankingUnidadDTO {

    private Long idUnidadDeVenta;
    private String nombreComercial;
    private String codigoUnico;
    private String tipoUnidad;
    private double recaudacion;
}