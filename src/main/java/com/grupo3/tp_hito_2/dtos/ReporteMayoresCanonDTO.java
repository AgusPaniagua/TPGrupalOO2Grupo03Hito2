package com.grupo3.tp_hito_2.dtos;

import com.grupo3.tp_hito_2.entities.TipoUnidad;

import lombok.Getter;

@Getter 
public class ReporteMayoresCanonDTO {

    private String nombreComercial;
    private String codigoUnico;
    private TipoUnidad tipo;
    private  double canon;

    public ReporteMayoresCanonDTO(String nombreComercial, String codigoUnico, TipoUnidad tipo, double canon){
        this.nombreComercial=nombreComercial;
        this.codigoUnico=codigoUnico;
        this.tipo=tipo;
        this.canon=canon;
    }
}
