package com.grupo3.tp_hito_2.services.implementation;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.grupo3.tp_hito_2.dtos.FoodTruckDTO;
import com.grupo3.tp_hito_2.dtos.PuestoDesarmableDTO;
import com.grupo3.tp_hito_2.dtos.RegistroUnidadDTO;
import com.grupo3.tp_hito_2.dtos.UnidadDeVentaDTO;
import com.grupo3.tp_hito_2.entities.Festival;
import com.grupo3.tp_hito_2.entities.FoodTruck;
import com.grupo3.tp_hito_2.entities.PuestoDesarmable;
import com.grupo3.tp_hito_2.entities.Rol;
import com.grupo3.tp_hito_2.entities.Staff;
import com.grupo3.tp_hito_2.entities.UnidadDeVenta;
import com.grupo3.tp_hito_2.entities.User;
import com.grupo3.tp_hito_2.entities.UserRole;
import com.grupo3.tp_hito_2.repositories.IFestivalRepository;
import com.grupo3.tp_hito_2.repositories.IUnidadDeVentaRepository;
import com.grupo3.tp_hito_2.repositories.IUserRepository;
import com.grupo3.tp_hito_2.services.IUnidadDeVentaService;
import java.util.List;
import java.util.Optional;

@Service("unidadDeVentaService")
public class UnidadDeVentaService implements IUnidadDeVentaService {
    private static final Logger logger = LoggerFactory.getLogger(UnidadDeVentaService.class);

    private final IUnidadDeVentaRepository unidadDeVentaRepository;

    private final ModelMapper modelMapper;

    private final IUserRepository userRepository;
    
    private final IFestivalRepository festivalRepository;
    
    public UnidadDeVentaService(IUnidadDeVentaRepository unidadDeVentaRepository, 
                                ModelMapper modelMapper,
                                IUserRepository userRepository,
                                IFestivalRepository festivalRepository){
        this.unidadDeVentaRepository = unidadDeVentaRepository;
        this.modelMapper = modelMapper;
        this.userRepository=userRepository;
        this.festivalRepository=festivalRepository;
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

    @Override 
    public UnidadDeVenta registrarConResponsable(RegistroUnidadDTO dto){
        if(userRepository.findByUsername(dto.getUsername()).isPresent()){
            throw new IllegalArgumentException("El nombre de usuario '" + dto.getUsername() + "' ya está en uso. Por favor, elegí otro.");
        }
        
        Festival festival = festivalRepository.findById(dto.getIdFestival())
        .orElseThrow(()->new IllegalArgumentException("No se encontro el festival con ID: "+dto.getIdFestival()));
        
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setEnabled(true);
        UserRole userRole = new UserRole(user,Rol.RESPONSABLE);
        user.getUserRoles().add(userRole);

        Staff responsable = new Staff();
        responsable.setNombre(dto.getNombre());
        responsable.setApellido(dto.getApellido());
        responsable.setDni(dto.getDni());
        responsable.setUser(user);
        

        UnidadDeVenta ud = new UnidadDeVenta(
            dto.getNombreComercial(),
            dto.getSuperficie(),
            dto.getCodigoUnico(),
            festival,
            responsable);
        
        return unidadDeVentaRepository.save(ud);
    }

}
