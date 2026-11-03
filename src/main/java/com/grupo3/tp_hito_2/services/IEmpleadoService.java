package com.grupo3.tp_hito_2.services;

import java.util.List;
import com.grupo3.tp_hito_2.dtos.EmpleadoDTO;
import com.grupo3.tp_hito_2.entities.Staff;

public interface IEmpleadoService {

    Staff altaEmpleado(String username, EmpleadoDTO dto);

    Staff reactivarEmpleado(String username, Long id);

    Staff modificarEmpleado(String username, EmpleadoDTO dto);

    Staff darDeBajaEmpleado(String username, EmpleadoDTO dto);

    EmpleadoDTO obtenerEmpleado(String username, Long id);

    List<EmpleadoDTO> listarEmpleados(String username);
}
