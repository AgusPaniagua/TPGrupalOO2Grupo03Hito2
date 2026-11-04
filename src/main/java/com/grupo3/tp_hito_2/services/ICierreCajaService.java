package com.grupo3.tp_hito_2.services;

import com.grupo3.tp_hito_2.dtos.CajaDelDiaDTO;
import com.grupo3.tp_hito_2.dtos.CierreCajaDTO;

// Caso de uso del Cajero: cerrar la caja al finalizar el día.
public interface ICierreCajaService {

    boolean esCajero(String username);

    CajaDelDiaDTO verCajaDelDia(String username);

    CierreCajaDTO cerrarCaja(String username);
}
