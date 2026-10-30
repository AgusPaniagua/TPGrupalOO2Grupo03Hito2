package com.grupo3.tp_hito_2.services;

import java.util.List;
import java.util.Optional;

import com.grupo3.tp_hito_2.dtos.RegistroUnidadDTO;
import com.grupo3.tp_hito_2.dtos.ReporteMayoresCanonDTO;
import com.grupo3.tp_hito_2.dtos.UnidadDeVentaDTO;
import com.grupo3.tp_hito_2.entities.UnidadDeVenta;
public interface IUnidadDeVentaService {

    List<UnidadDeVenta> getAll();
    
    Optional<UnidadDeVenta> findById(Long id);

    Optional<UnidadDeVenta> findByCodigoUnico(String codigoUnico);

    UnidadDeVenta insertOrUpdate(UnidadDeVenta unidadDeVenta);

    boolean remove(Long id);

    UnidadDeVentaDTO convertirADTO(UnidadDeVenta unidadDeVenta);

    UnidadDeVenta registrarConResponsable(RegistroUnidadDTO dto);

    List<ReporteMayoresCanonDTO> generarReporteMayoresCanon(Long idFestival, int x);

   
} 
