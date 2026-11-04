package com.grupo3.tp_hito_2.services;

import com.grupo3.tp_hito_2.dtos.ReciboSueldoDTO;
import com.grupo3.tp_hito_2.entities.Festival;
import com.grupo3.tp_hito_2.entities.Staff;

public interface ILiquidacionService {

    // Calcula lo que cobra un empleado por trabajar en un festival.
    ReciboSueldoDTO calcularHaberes(Staff staff, Festival festival);
}
