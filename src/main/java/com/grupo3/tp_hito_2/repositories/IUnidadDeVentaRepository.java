package com.grupo3.tp_hito_2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;
import com.grupo3.tp_hito_2.entities.UnidadDeVenta;

@Repository("unidadDeVentaRepository")
public interface IUnidadDeVentaRepository extends JpaRepository<UnidadDeVenta, Long> {

    public abstract Optional<UnidadDeVenta> findByCodigoUnico(String codigoUnico);

    public List<UnidadDeVenta> findByFestival_IdFestival(Long id);

}
