package com.grupo3.tp_hito_2.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @NoArgsConstructor
@ToString (onlyExplicitlyIncluded = true)
public class Plato {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private Long idPlato;

    @ToString.Include
    private String nombre;

    @ToString.Include
    private double precio;

    @ToString.Include
    private double costoDeProduccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidad_de_venta_id")
    private UnidadDeVenta unidadDeVenta;

    public Plato(String nombre, double precio, double costoDeProduccion){
        this.nombre=nombre;
        this.precio=precio;
        this.costoDeProduccion=costoDeProduccion;
    }
}
