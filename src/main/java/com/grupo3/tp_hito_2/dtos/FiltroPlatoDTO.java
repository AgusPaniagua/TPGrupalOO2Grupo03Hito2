package com.grupo3.tp_hito_2.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class FiltroPlatoDTO {
    private String nombre;
    private Double precioMinimo;
    private Double precioMaximo;
}
