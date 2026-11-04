package com.grupo3.tp_hito_2.dtos;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EmpleadoListadoDTO {
    private Long idStaff;
    private String nombre;
    private String apellido;
    private int dni;
    private String tipo;
    private String detalle;
    private LocalDate fechaDeIngreso;
    private int antiguedad;
}
