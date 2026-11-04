package com.grupo3.tp_hito_2.services;

import com.grupo3.tp_hito_2.dtos.ReciboSueldoDTO;

public interface IReciboSueldoPdfService {

    // Genera el recibo de sueldo en formato PDF (bytes listos para descargar).
    byte[] generarReciboDeSueldo(ReciboSueldoDTO recibo);
}
