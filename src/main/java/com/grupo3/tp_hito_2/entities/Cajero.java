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
public class Cajero extends Staff {

    @ToString.Include
    private String turnoDeTrabajo;

    public Cajero(String nombre, String apellido, int dni, LocalDate fechaDeNacimiento,
                  LocalDate fechaDeIngreso, double sueldoBase, UnidadDeVenta unidadDeVenta,
                  User user,String turnoDeTrabajo) {
        super(nombre, apellido, dni, fechaDeNacimiento, fechaDeIngreso, sueldoBase, unidadDeVenta,user);
        this.turnoDeTrabajo = turnoDeTrabajo;
    }
}
