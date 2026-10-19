package com.grupo3.tp_hito_2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grupo3.tp_hito_2.entities.Costo;

import java.util.Optional;

@Repository
public interface ICostoRepository extends JpaRepository<Costo, Long> {

    Optional<Costo> findByFestival_IdFestival(Long idFestival);

}
