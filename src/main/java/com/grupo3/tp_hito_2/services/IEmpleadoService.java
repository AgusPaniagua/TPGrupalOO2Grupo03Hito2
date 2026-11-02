package com.grupo3.tp_hito_2.services;

import com.grupo3.tp_hito_2.dtos.EmpleadoDTO;
import com.grupo3.tp_hito_2.entities.Staff;

public interface IEmpleadoService {

    Staff altaEmpleado(String username, EmpleadoDTO dto);

}
