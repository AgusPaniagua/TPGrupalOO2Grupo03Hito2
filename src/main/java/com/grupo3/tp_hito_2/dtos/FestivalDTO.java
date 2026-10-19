package com.grupo3.tp_hito_2.dtos;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FestivalDTO {
    private long idFestival;
    private String nombre;
    private LocalDate temporada;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private CostoDTO costo;
    private List<UnidadDeVentaDTO> unidadesDeVenta;

    public FestivalDTO(Long idFestival, String nombre, LocalDate temporada, LocalDate fechaInicio, LocalDate fechaFin,
                       CostoDTO costo, List<UnidadDeVentaDTO> unidadesDeVenta) {
        this.idFestival = idFestival;
        this.nombre = nombre;
        this.temporada = temporada;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.costo = costo;
        this.unidadesDeVenta = unidadesDeVenta;
    }

}
