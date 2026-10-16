package com.grupo3.tp_hito_2.services;

import java.util.List;
import com.grupo3.tp_hito_2.dtos.PlatoDTO;
import com.grupo3.tp_hito_2.entities.Plato;
import com.grupo3.tp_hito_2.entities.UnidadDeVenta;
import java.util.Optional;

public interface IPlatoService {

    List<Plato> getAll();

    Optional<Plato> findById(Long id);

    Plato insertOrUpdate(Plato plato);

    boolean remove(Long id);

    Optional<Plato> findByNombreAndUnidadDeVenta(String nombre, UnidadDeVenta unidadDeVenta);
    
    PlatoDTO convertirADTO(Plato plato);
} 
