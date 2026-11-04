package com.grupo3.tp_hito_2.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

// Parámetros de la liquidación de haberes. Se leen de application.properties
// para no tener valores "hardcodeados" dentro de los métodos de cálculo.
@Getter
@Component
public class LiquidacionConfig {

    private final double sueldoBase;
    private final double plusPorAnioDeAntiguedad;
    private final int diasPorIntervalo;
    private final double porcentajePorIntervalo;

    public LiquidacionConfig(
            @Value("${liquidacion.sueldo-base:100000}") double sueldoBase,
            @Value("${liquidacion.plus-por-anio-antiguedad:5000}") double plusPorAnioDeAntiguedad,
            @Value("${liquidacion.dias-por-intervalo:15}") int diasPorIntervalo,
            @Value("${liquidacion.porcentaje-por-intervalo:0.5}") double porcentajePorIntervalo) {
        this.sueldoBase = sueldoBase;
        this.plusPorAnioDeAntiguedad = plusPorAnioDeAntiguedad;
        this.diasPorIntervalo = diasPorIntervalo;
        this.porcentajePorIntervalo = porcentajePorIntervalo;
    }
}
