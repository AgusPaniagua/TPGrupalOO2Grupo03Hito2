package com.grupo3.tp_hito_2.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class CostoDTO {
    private long idCosto;
    private BigDecimal porSuperficie;
    private BigDecimal porMontaje;
    private BigDecimal plusPorElectricidad;
    private BigDecimal sueldoBase;

    public CostoDTO (long idCosto,BigDecimal porSuperficie,BigDecimal porMontaje,
                     BigDecimal plusPorElectricidad,BigDecimal sueldoBase){
        this.setIdCosto(idCosto);
        this.porSuperficie=sueldoBase;
        this.porMontaje=porMontaje;
        this.plusPorElectricidad=plusPorElectricidad;
        this.sueldoBase=sueldoBase;
    }
}
