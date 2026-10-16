package com.grupo3.tp_hito_2.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor 
public class PlatoDTO {
    private Long idPlato;
    private String nombre;
    private double precio;
    private double costoDeProduccion;
    private Long idUnidadDeVenta;

    public PlatoDTO(Long idPlato, String nombre, double precio, double costoDeProduccion, Long idUnidadDeVenta){
        this.setIdPlato(idPlato);
        this.nombre=nombre;
        this.precio=precio;
        this.costoDeProduccion=costoDeProduccion;
        this.idUnidadDeVenta=idUnidadDeVenta;
    }
}
