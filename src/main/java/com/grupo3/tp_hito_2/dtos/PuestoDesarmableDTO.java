package com.grupo3.tp_hito_2.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor 
public class PuestoDesarmableDTO extends UnidadDeVentaDTO{
    private int cantidadDeCarpas;
    private int tiempo;

    public PuestoDesarmableDTO(Long id, String nombreComercial, double superficie, String codigoUnico, Long idFestival, Long idResponsable,int cantidadDeCarpas, int tiempo){
        super(id,nombreComercial,superficie,codigoUnico,idFestival,idResponsable);
        this.cantidadDeCarpas=cantidadDeCarpas;
        this.tiempo=tiempo;
    }
}
