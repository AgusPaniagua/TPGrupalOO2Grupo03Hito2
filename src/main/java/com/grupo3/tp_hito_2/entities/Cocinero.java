package com.grupo3.tp_hito_2.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @NoArgsConstructor
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
public class Cocinero extends Staff {

    @ToString.Include
    private String especialidad;

    @ToString.Include
    private int plusFijo;

    public Cocinero(String nombre, String apellido, int dni, LocalDate fechaDeNacimiento,
                    LocalDate fechaDeIngreso, double sueldoBase, UnidadDeVenta unidadDeVenta,
                    String especialidad, int plusFijo) {
        super(nombre, apellido, dni, fechaDeNacimiento, fechaDeIngreso, sueldoBase, unidadDeVenta);
        this.especialidad = especialidad;
        this.plusFijo = plusFijo;
    }
}
