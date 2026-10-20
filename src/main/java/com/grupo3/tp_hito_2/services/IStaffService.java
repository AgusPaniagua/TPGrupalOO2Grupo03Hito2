package com.grupo3.tp_hito_2.services;

import java.util.List;
import java.util.Optional;

import com.grupo3.tp_hito_2.dtos.StaffDTO;
import com.grupo3.tp_hito_2.entities.Staff;

public interface IStaffService {

    List<Staff> getAll();

    Optional<Staff> findById(Long id);

    Optional<Staff> findByDni(int dni);

    List<Staff> findByUnidadDeVenta(Long idUnidadDeVenta);

    Staff insertOrUpdate(Staff staff);

    boolean remove(Long id);

    StaffDTO convertirADTO(Staff staff);
}
