package com.grupo3.tp_hito_2.services.implementation;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.grupo3.tp_hito_2.dtos.FoodTruckDTO;
import com.grupo3.tp_hito_2.dtos.PuestoDesarmableDTO;
import com.grupo3.tp_hito_2.dtos.RegistroUnidadDTO;
import com.grupo3.tp_hito_2.dtos.ReporteMayoresCanonDTO;
import com.grupo3.tp_hito_2.dtos.UnidadDeVentaDTO;
import com.grupo3.tp_hito_2.entities.Festival;
import com.grupo3.tp_hito_2.entities.FoodTruck;
import com.grupo3.tp_hito_2.entities.PuestoDesarmable;
import com.grupo3.tp_hito_2.entities.Rol;
import com.grupo3.tp_hito_2.entities.Staff;
import com.grupo3.tp_hito_2.entities.TipoUnidad;
import com.grupo3.tp_hito_2.entities.UnidadDeVenta;
import com.grupo3.tp_hito_2.entities.User;
import com.grupo3.tp_hito_2.entities.UserRole;
import com.grupo3.tp_hito_2.repositories.IFestivalRepository;
import com.grupo3.tp_hito_2.repositories.IStaffRepository;
import com.grupo3.tp_hito_2.repositories.IUnidadDeVentaRepository;
import com.grupo3.tp_hito_2.repositories.IUserRepository;
import com.grupo3.tp_hito_2.services.IUnidadDeVentaService;

import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service("unidadDeVentaService")
public class UnidadDeVentaService implements IUnidadDeVentaService {
    private static final Logger logger = LoggerFactory.getLogger(UnidadDeVentaService.class);

    private final IUnidadDeVentaRepository unidadDeVentaRepository;

    private final ModelMapper modelMapper;

    private final IUserRepository userRepository;
    
    private final IFestivalRepository festivalRepository;

    private final IStaffRepository staffRepository;
    
    public UnidadDeVentaService(IUnidadDeVentaRepository unidadDeVentaRepository, 
                                ModelMapper modelMapper,
                                IUserRepository userRepository,
                                IFestivalRepository festivalRepository,
                                IStaffRepository staffRepository){
        this.unidadDeVentaRepository = unidadDeVentaRepository;
        this.modelMapper = modelMapper;
        this.userRepository=userRepository;
        this.festivalRepository=festivalRepository;
        this.staffRepository=staffRepository;
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

    @Autowired 
    private PasswordEncoder passwordEncoder;


    @Override
    @Transactional 
    public UnidadDeVenta registrarConResponsable(RegistroUnidadDTO dto){
        if(userRepository.findByUsername(dto.getUsername()).isPresent()){
            throw new IllegalArgumentException("El nombre de usuario '" + dto.getUsername() + "' ya está en uso. Por favor, elegí otro.");
        }
        if(staffRepository.findByDni(dto.getDni()).isPresent()){
            throw new IllegalArgumentException("Ya existe un responsable con el DNI " + dto.getDni());
        }
        if(unidadDeVentaRepository.findByCodigoUnico(dto.getCodigoUnico()).isPresent()){
            throw new IllegalArgumentException("Ya existe una unidad con el codigo unico " + dto.getCodigoUnico());
        }
        Festival festival = festivalRepository.findById(dto.getIdFestival())
        .orElseThrow(()->new IllegalArgumentException("No se encontro el festival con ID: "+dto.getIdFestival()));
        
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEnabled(true);
        UserRole userRole = new UserRole(user,Rol.RESPONSABLE);
        user.getUserRoles().add(userRole);
        userRepository.save(user);

        Staff responsable = new Staff();
        responsable.setNombre(dto.getNombre());
        responsable.setApellido(dto.getApellido());
        responsable.setDni(dto.getDni());
        responsable.setUser(user);
        staffRepository.save(responsable);
        

        UnidadDeVenta ud = switch(dto.getTipo()){
            case FOOD_TRUCK -> new FoodTruck(
                dto.getNombreComercial(),
                dto.getSuperficie(),
                dto.getCodigoUnico(),
                festival,
                user,
                dto.getPatente(),
                dto.isConexionElectrica());
            case PUESTO_DESARMABLE -> new PuestoDesarmable(
                dto.getNombreComercial(),
                dto.getSuperficie(),
                dto.getCodigoUnico(),
                festival,
                user,
                dto.getCantidadDeCarpas(),
                dto.getTiempoDeMontaje());
        };
        
        return unidadDeVentaRepository.save(ud);
    }


    @Override
    @Transactional(readOnly=true)
    public List<ReporteMayoresCanonDTO> generarReporteMayoresCanon(Long idFestival, int x){
        if(x<=0){
            throw new IllegalArgumentException("El limite no puede ser 0 o negativo");
        }
        festivalRepository.findById(idFestival)
        .orElseThrow(()->new IllegalArgumentException("No se encontro el festival con ID: "+idFestival));
        List<UnidadDeVenta> unidades = unidadDeVentaRepository.findByFestival_IdFestival(idFestival);
        
        return unidades.stream().map(unidad -> new ReporteMayoresCanonDTO(
                unidad.getNombreComercial(),
                unidad.getCodigoUnico(),  
                unidad.getTipoUnidad(),  
                unidad.calcularCanon()))
                .sorted(Comparator.comparingDouble(ReporteMayoresCanonDTO::getCanon).reversed())
                .limit(x)
                .toList();
    }
    @Override
    @Transactional(readOnly = true)
    public List<UnidadDeVentaDTO> traerPorFestival(Long idFestival) {

        List<UnidadDeVenta> unidades =
                unidadDeVentaRepository
                        .findByFestival_IdFestival(idFestival);

        return unidades.stream()
                .map(this::convertirADTO)
                .toList();
    }

}
