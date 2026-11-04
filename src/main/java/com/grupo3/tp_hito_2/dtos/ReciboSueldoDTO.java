package com.grupo3.tp_hito_2.dtos;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Resultado de la liquidación de haberes de un empleado en un festival (no persiste).
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReciboSueldoDTO {
    private Long idStaff;
    private String nombreCompleto;
    private int dni;
    private String tipoPersonal;
    private String detalleRol;
    private int aniosAntiguedad;

    private Long idFestival;
    private String nombreFestival;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private long diasFestival;
    private String estadoFestival;

    private String nombreUnidad;
    private String codigoUnidad;

    private double sueldoBase;
    private double plusCategoria;
    private double plusAntiguedad;
    private double subtotal;
    private int cantidadIntervalos;
    private double porcentajeAplicado;
    private double totalACobrar;
}
