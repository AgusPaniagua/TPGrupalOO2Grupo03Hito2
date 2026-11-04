package com.grupo3.tp_hito_2.dtos;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MiInformacionDTO {
    private String nombre;
    private String apellido;
    private int dni;
    private LocalDate fechaDeNacimiento;
    private LocalDate fechaDeIngreso;
    private int antiguedad;
    private String tipoPersonal;
    private String detalleRol;
    private List<ParticipacionDTO> participaciones;
}
