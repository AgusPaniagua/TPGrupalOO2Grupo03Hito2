package com.grupo3.tp_hito_2.services;

import com.grupo3.tp_hito_2.dtos.CostoDTO;
import com.grupo3.tp_hito_2.entities.Costo;

import java.util.List;
import java.util.Optional;

public interface ICostoService {

    List<Costo> getAll();

    Optional<Costo> findById(Long id);

    Costo insertOrUpdate(Costo costo);

    boolean remove(Long id);

    CostoDTO convertirADTO(Costo costo);
}
