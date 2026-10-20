package com.grupo3.tp_hito_2.dtos;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class CajeroDTO extends StaffDTO {
    private String turnoDeTrabajo;

    public CajeroDTO(Long idStaff, String nombre, String apellido, int dni, LocalDate fechaDeNacimiento,
                     LocalDate fechaDeIngreso, int antiguedad, double sueldoBase, Long idUnidadDeVenta,
                     String turnoDeTrabajo) {
        super(idStaff, nombre, apellido, dni, fechaDeNacimiento, fechaDeIngreso, antiguedad, sueldoBase, idUnidadDeVenta);
        this.turnoDeTrabajo = turnoDeTrabajo;
    }
}
