package com.grupo3.tp_hito_2.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grupo3.tp_hito_2.entities.Staff;

@Repository("staffRepository")
public interface IStaffRepository extends JpaRepository<Staff, Long> {

    Optional<Staff> findByDni(int dni);

    List<Staff> findByUnidadDeVenta_IdUnidadDeVenta(Long idUnidadDeVenta);
}
