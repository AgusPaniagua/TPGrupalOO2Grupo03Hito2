package com.grupo3.tp_hito_2.dtos;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class CocineroDTO extends StaffDTO {
    private String especialidad;
    private int plusFijo;

    public CocineroDTO(Long idStaff, String nombre, String apellido, int dni, LocalDate fechaDeNacimiento,
                       LocalDate fechaDeIngreso, int antiguedad, double sueldoBase, Long idUnidadDeVenta,
                       String especialidad, int plusFijo) {
        super(idStaff, nombre, apellido, dni, fechaDeNacimiento, fechaDeIngreso, antiguedad, sueldoBase, idUnidadDeVenta);
        this.especialidad = especialidad;
        this.plusFijo = plusFijo;
    }
}
