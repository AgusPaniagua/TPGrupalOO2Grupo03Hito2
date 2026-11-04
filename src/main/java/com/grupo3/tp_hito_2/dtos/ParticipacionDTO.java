package com.grupo3.tp_hito_2.dtos;

import java.time.LocalDate;

import com.grupo3.tp_hito_2.entities.TipoUnidad;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Festival y unidad de venta en la que trabaja (o trabajó) un empleado.
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ParticipacionDTO {
    private Long idFestival;
    private String nombreFestival;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado;
    private boolean activo;

    private Long idUnidadDeVenta;
    private String nombreUnidad;
    private String codigoUnico;
    private TipoUnidad tipoUnidad;
}
