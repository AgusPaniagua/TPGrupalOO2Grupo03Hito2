package com.grupo3.tp_hito_2.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor 
public class UnidadDeVentaDTO {
    private Long idUnidadDeVenta;
    private String nombreComercial;
    private double superficie;
    private String codigoUnico;
    private Long idFestival;

    public UnidadDeVentaDTO(Long idUnidadDeVenta, String nombreComercial, double superficie, String codigoUnico, Long idFestival){
        this.setIdUnidadDeVenta(idUnidadDeVenta);
        this.nombreComercial=nombreComercial;
        this.superficie=superficie;
        this.codigoUnico=codigoUnico;
        this.idFestival=idFestival;
    }
}
