package com.grupo3.tp_hito_2.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CierreCajaDTO {
    private Long idCierreCaja;
    private LocalDate fecha;
    private LocalDateTime fechaHoraCierre;
    private String nombreUnidad;
    private String cerradoPor;
    private double totalRecaudado;
    private int cantidadPedidos;
}
