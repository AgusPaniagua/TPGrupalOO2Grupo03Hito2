package com.grupo3.tp_hito_2.entities;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @NoArgsConstructor 
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
public class PuestoDesarmable extends UnidadDeVenta{
    
    @ToString.Include
    private int cantidadDeCarpas;
    
    @ToString.Include
    private int tiempo;

    public PuestoDesarmable(String nombreComercial, double superficie, String codigoUnico, Festival festival, int cantidadDeCarpas, int tiempo){
        super(nombreComercial, superficie, codigoUnico, festival);
        this.cantidadDeCarpas=cantidadDeCarpas;
        this.tiempo=tiempo;
    }


}
