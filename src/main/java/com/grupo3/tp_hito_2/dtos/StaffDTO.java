package com.grupo3.tp_hito_2.dtos;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class StaffDTO {
    private Long idStaff;
    private String nombre;
    private String apellido;
    private int dni;
    private LocalDate fechaDeNacimiento;
    private LocalDate fechaDeIngreso;
    private int antiguedad;
    private double sueldoBase;
    private Long idUnidadDeVenta;

    public StaffDTO(Long idStaff, String nombre, String apellido, int dni, LocalDate fechaDeNacimiento,
                    LocalDate fechaDeIngreso, int antiguedad, double sueldoBase, Long idUnidadDeVenta) {
        this.idStaff = idStaff;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.fechaDeNacimiento = fechaDeNacimiento;
        this.fechaDeIngreso = fechaDeIngreso;
        this.antiguedad = antiguedad;
        this.sueldoBase = sueldoBase;
        this.idUnidadDeVenta = idUnidadDeVenta;
    }
}
