package com.grupo3.tp_hito_2.dtos;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Criterios de búsqueda de empleados. Todos son opcionales: se combinan con AND.
@Getter @Setter @NoArgsConstructor
public class FiltroStaffDTO {
    private String nombre;
    private String apellido;
    private Integer dni;
    private String tipo;          // "COCINERO", "CAJERO" o vacío (todos)
    private String turno;         // solo cajeros
    private String especialidad;  // solo cocineros

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ingresoDesde;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ingresoHasta;

    private Integer antiguedadMinima;
}
