package com.grupo3.tp_hito_2.dtos;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Estado de la caja de la unidad del cajero en el día de hoy (no persiste).
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CajaDelDiaDTO {
    private String nombreUnidad;
    private String nombreFestival;
    private LocalDate fecha;
    private boolean festivalActivo;
    private double totalDelDia;
    private int cantidadPedidos;
    private CierreCajaDTO cierreDeHoy;
    private List<CierreCajaDTO> historial;
}
