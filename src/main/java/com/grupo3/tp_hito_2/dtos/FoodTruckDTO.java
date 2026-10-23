package com.grupo3.tp_hito_2.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor 
public class FoodTruckDTO extends UnidadDeVentaDTO{
    private String patente;
    private boolean conexionElectrica;

    public FoodTruckDTO(Long id, String nombreComercial, double superficie, String codigoUnico, Long idFestival, Long idResponsable, String patente, boolean conexionElectrica){
        super(id,nombreComercial,superficie,codigoUnico,idFestival,idResponsable);
        this.patente=patente;
        this.conexionElectrica=conexionElectrica;
    }
}
