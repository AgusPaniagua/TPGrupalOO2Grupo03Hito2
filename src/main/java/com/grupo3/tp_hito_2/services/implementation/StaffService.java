package com.grupo3.tp_hito_2.services.implementation;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.grupo3.tp_hito_2.dtos.CajeroDTO;
import com.grupo3.tp_hito_2.dtos.CocineroDTO;
import com.grupo3.tp_hito_2.dtos.StaffDTO;
import com.grupo3.tp_hito_2.entities.Cajero;
import com.grupo3.tp_hito_2.entities.Cocinero;
import com.grupo3.tp_hito_2.entities.Staff;
import com.grupo3.tp_hito_2.repositories.IStaffRepository;
import com.grupo3.tp_hito_2.services.IStaffService;

@Service("staffService")
public class StaffService implements IStaffService {

    private static final Logger logger = LoggerFactory.getLogger(StaffService.class);

    private final IStaffRepository staffRepository;
    private final ModelMapper modelMapper;

    public StaffService(IStaffRepository staffRepository, ModelMapper modelMapper) {
        this.staffRepository = staffRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<Staff> getAll() {
        return staffRepository.findAll();
    }

    @Override
    public Optional<Staff> findById(Long id) {
        return staffRepository.findById(id);
    }

    @Override
    public Optional<Staff> findByDni(int dni) {
        return staffRepository.findByDni(dni);
    }

    @Override
    public List<Staff> findByUnidadDeVenta(Long idUnidadDeVenta) {
        return staffRepository.findByUnidadDeVenta_IdUnidadDeVenta(idUnidadDeVenta);
    }

    @Override
    public Staff insertOrUpdate(Staff staff) {
        return staffRepository.save(staff);
    }

    @Override
    public boolean remove(Long id) {
        try {
            staffRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException | DataIntegrityViolationException e) {
            logger.warn("No se pudo eliminar el staff con id {}", id, e);
            return false;
        }
    }

    @Override
    public StaffDTO convertirADTO(Staff staff) {
        if (staff instanceof Cocinero) {
            return modelMapper.map(staff, CocineroDTO.class);
        } else if (staff instanceof Cajero) {
            return modelMapper.map(staff, CajeroDTO.class);
        }
        return modelMapper.map(staff, StaffDTO.class);
    }
}
