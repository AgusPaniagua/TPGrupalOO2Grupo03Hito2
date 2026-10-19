package com.grupo3.tp_hito_2.repositories;

import com.grupo3.tp_hito_2.entities.Festival;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("Festival")
public interface IFestivalRepository extends JpaRepository<Festival, Long> {

    Optional<Festival> findByNombre(String nombre);
}
