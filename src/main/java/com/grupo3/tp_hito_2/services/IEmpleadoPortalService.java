package com.grupo3.tp_hito_2.services;

import java.util.List;
import java.util.Optional;

import com.grupo3.tp_hito_2.dtos.MiInformacionDTO;
import com.grupo3.tp_hito_2.dtos.ParticipacionDTO;
import com.grupo3.tp_hito_2.dtos.ReciboSueldoDTO;

// Casos de uso del rol EMPLEADO. Siempre operan sobre el usuario autenticado (username).
public interface IEmpleadoPortalService {

    MiInformacionDTO verMiInformacion(String username);

    List<ReciboSueldoDTO> verSueldosPorFestival(String username);

    ReciboSueldoDTO reciboDeFestival(String username, Long idFestival);

    Optional<ParticipacionDTO> verAsignacionActual(String username);
}
