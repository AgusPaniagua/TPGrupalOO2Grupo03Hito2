package com.grupo3.tp_hito_2.repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.grupo3.tp_hito_2.entities.Plato;
import com.grupo3.tp_hito_2.entities.UnidadDeVenta;
import java.util.List;

@Repository("platoRepository")
public interface IPlatoRepository extends JpaRepository<Plato, Long>{

    public abstract Optional<Plato> findByNombreAndUnidadDeVenta(String nombre, UnidadDeVenta unidadDeVenta);

    List<Plato> findByUnidadDeVenta(UnidadDeVenta unidadDeVenta);
}
