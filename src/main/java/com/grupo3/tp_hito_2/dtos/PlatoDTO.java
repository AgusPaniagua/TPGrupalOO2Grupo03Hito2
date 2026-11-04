package com.grupo3.tp_hito_2.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor 
public class PlatoDTO {
    
    private Long idPlato;
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    @Positive (message = "El precio no puede ser mayor a 0")
    private double precio;
    @PositiveOrZero(message = "El costo de produccion no puede ser negativo")
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
