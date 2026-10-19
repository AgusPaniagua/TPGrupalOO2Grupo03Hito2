package com.grupo3.tp_hito_2.entities;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @NoArgsConstructor
@ToString (onlyExplicitlyIncluded = true)
@Inheritance(strategy = InheritanceType.JOINED)
public class Costo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private long idCosto;
    @ToString.Include
    private BigDecimal porSuperficie;
    @ToString.Include
    private BigDecimal porMontaje;
    @ToString.Include
    private BigDecimal plusPorElectricidad;
    @ToString.Include
    private  BigDecimal sueldoBase;
    @OneToOne(mappedBy = "costo")
    private Festival festival;

    public Costo( BigDecimal porSuperficie, BigDecimal porMontaje,BigDecimal plusPorElectricidad,BigDecimal sueldoBase, Festival festivalParametro){
        this.porSuperficie=porSuperficie;
        this.porMontaje=porMontaje;
        this.plusPorElectricidad=plusPorElectricidad;
        this.sueldoBase=sueldoBase;
        this.festival=festivalParametro;
    }
}
