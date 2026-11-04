package com.grupo3.tp_hito_2.services;

import java.util.List;

import com.grupo3.tp_hito_2.dtos.EmpleadoListadoDTO;
import com.grupo3.tp_hito_2.dtos.FiltroPlatoDTO;
import com.grupo3.tp_hito_2.dtos.FiltroStaffDTO;
import com.grupo3.tp_hito_2.dtos.LiquidacionUnidadDTO;
import com.grupo3.tp_hito_2.dtos.PlatoDTO;

// Consultas del rol RESPONSABLE sobre la unidad de venta que tiene asignada.
public interface IResponsableConsultaService {

    // CU Responsable 2: filtros por uno o varios atributos (sin traer eliminados lógicamente)
    List<EmpleadoListadoDTO> filtrarEmpleados(String username, FiltroStaffDTO filtro);

    List<PlatoDTO> filtrarPlatos(String username, FiltroPlatoDTO filtro);

    // CU Responsable 4: liquidación de haberes del personal de la unidad
    LiquidacionUnidadDTO liquidarHaberes(String username);
}
