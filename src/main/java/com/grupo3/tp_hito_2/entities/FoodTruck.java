package com.grupo3.tp_hito_2.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity 
@Getter @Setter @NoArgsConstructor
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
public class FoodTruck extends UnidadDeVenta {

    @Column(unique = true, nullable = false)
    @ToString.Include
    private String patente;

    @ToString.Include
    private boolean conexionElectrica;

    public FoodTruck(String nombreComercial, double superficie, String codigoUnico, Festival festival, String patente, boolean conexionElectrica){
        super(nombreComercial, superficie, codigoUnico, festival);
        this.patente=patente;
        this.conexionElectrica=conexionElectrica;
    }
}
