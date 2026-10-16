package com.grupo3.tp_hito_2.services.implementation;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.grupo3.tp_hito_2.dtos.FoodTruckDTO;
import com.grupo3.tp_hito_2.dtos.PuestoDesarmableDTO;
import com.grupo3.tp_hito_2.dtos.UnidadDeVentaDTO;
import com.grupo3.tp_hito_2.entities.FoodTruck;
import com.grupo3.tp_hito_2.entities.PuestoDesarmable;
import com.grupo3.tp_hito_2.entities.UnidadDeVenta;
import com.grupo3.tp_hito_2.repositories.IUnidadDeVentaRepository;
import com.grupo3.tp_hito_2.services.IUnidadDeVentaService;
import java.util.List;
import java.util.Optional;

@Service("unidadDeVentaService")
public class UnidadDeVentaService implements IUnidadDeVentaService {
    private static final Logger logger = LoggerFactory.getLogger(UnidadDeVentaService.class);

    private final IUnidadDeVentaRepository unidadDeVentaRepository;

    private final ModelMapper modelMapper;
    
    public UnidadDeVentaService(IUnidadDeVentaRepository unidadDeVentaRepository, ModelMapper modelMapper){
        this.unidadDeVentaRepository = unidadDeVentaRepository;
        this.modelMapper = modelMapper;
    }

    public List<UnidadDeVenta> getAll(){
        return unidadDeVentaRepository.findAll();
    }
    
    public Optional<UnidadDeVenta> findById(Long id){
        return unidadDeVentaRepository.findById(id);
    }

    public Optional<UnidadDeVenta> findByCodigoUnico(String codigoUnico){
        return unidadDeVentaRepository.findByCodigoUnico(codigoUnico);
    }

    public UnidadDeVenta insertOrUpdate(UnidadDeVenta unidadDeVenta){
        return unidadDeVentaRepository.save(unidadDeVenta);
    }

    public boolean remove(Long id){
        try{
            unidadDeVentaRepository.deleteById(id);
            return true;
        }catch(EmptyResultDataAccessException | DataIntegrityViolationException e){
            logger.warn("No se pudo eliminar la unidad de venta con id {}",id, e);
            return false;
        }
    }

    public UnidadDeVentaDTO convertirADTO(UnidadDeVenta unidadDeVenta){
        if(unidadDeVenta instanceof FoodTruck){
            return modelMapper.map(unidadDeVenta, FoodTruckDTO.class);
        }else if(unidadDeVenta instanceof PuestoDesarmable){
            return modelMapper.map(unidadDeVenta, PuestoDesarmableDTO.class);
        }
        return modelMapper.map(unidadDeVenta, UnidadDeVentaDTO.class);
    }

}
