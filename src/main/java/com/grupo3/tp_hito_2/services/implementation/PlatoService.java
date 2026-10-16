package com.grupo3.tp_hito_2.services.implementation;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import com.grupo3.tp_hito_2.repositories.IPlatoRepository;
import com.grupo3.tp_hito_2.services.IPlatoService;
import com.grupo3.tp_hito_2.dtos.PlatoDTO;
import com.grupo3.tp_hito_2.entities.Plato;
import com.grupo3.tp_hito_2.entities.UnidadDeVenta;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
@Service("platoService")
public class PlatoService implements IPlatoService{
    private static final Logger logger = LoggerFactory.getLogger(PlatoService.class);

    private final IPlatoRepository platoRepository;
    
    private final ModelMapper modelMapper;

    public PlatoService(IPlatoRepository platoRepository, ModelMapper modelMapper){
        this.platoRepository = platoRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<Plato> getAll(){
        return platoRepository.findAll();
    }

    @Override 
    public Optional<Plato> findById(Long id){
        return platoRepository.findById(id);
    }

    @Override 
    public Plato insertOrUpdate(Plato plato){
        return platoRepository.save(plato);
    }

    @Override
    public boolean remove(Long id){
        try{
            platoRepository.deleteById(id);
            return true;
        }catch(EmptyResultDataAccessException | DataIntegrityViolationException e){
            logger.warn("No se pudo eliminar el plato con id {}", id, e);
            return false;
        }catch(DataAccessException e){
            logger.error("Error de base de datos al eliminar plato con id {}", id, e);
            return false;
        }
    }

    @Override
    public Optional<Plato> findByNombreAndUnidadDeVenta(String nombre,UnidadDeVenta unidadDeVenta){
        return platoRepository.findByNombreAndUnidadDeVenta(nombre, unidadDeVenta);
    }
    
    @Override
    public PlatoDTO convertirADTO(Plato plato){
        return modelMapper.map(plato, PlatoDTO.class);
    }
}
