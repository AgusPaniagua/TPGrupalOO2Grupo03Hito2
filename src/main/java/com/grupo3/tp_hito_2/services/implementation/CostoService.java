package com.grupo3.tp_hito_2.services.implementation;

import com.grupo3.tp_hito_2.dtos.CostoDTO;
import com.grupo3.tp_hito_2.entities.Costo;
import com.grupo3.tp_hito_2.repositories.ICostoRepository;
import com.grupo3.tp_hito_2.services.ICostoService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;



@Service("costoService")
public class CostoService implements ICostoService {

    private static final Logger logger =
            LoggerFactory.getLogger(CostoService.class);

    private final ICostoRepository costoRepository;
    private final ModelMapper modelMapper;

    public CostoService(ICostoRepository costoRepository,
                        ModelMapper modelMapper) {
        this.costoRepository = costoRepository;
        this.modelMapper = modelMapper;
    }

    public List<Costo> getAll() {
        return costoRepository.findAll();
    }

    public Optional<Costo> findById(Long id) {

        return costoRepository.findById(id);
    }

    public Costo insertOrUpdate(Costo costo) {

        return costoRepository.save(costo);
    }

    public boolean remove(Long id) {
        try {
            costoRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException |
                 DataIntegrityViolationException e) {
            logger.warn("No se pudo eliminar el costo con id {}", id, e);
            return false;
        }
    }

    public CostoDTO convertirADTO(Costo costo) {
        return modelMapper.map(costo, CostoDTO.class);
    }
}
