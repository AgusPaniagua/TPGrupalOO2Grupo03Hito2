package com.grupo3.tp_hito_2.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grupo3.tp_hito_2.entities.CierreCaja;

@Repository
public interface ICierreCajaRepository extends JpaRepository<CierreCaja, Long> {

    Optional<CierreCaja> findByUnidadDeVenta_IdUnidadDeVentaAndFecha(Long idUnidadDeVenta, LocalDate fecha);

    List<CierreCaja> findTop10ByUnidadDeVenta_IdUnidadDeVentaOrderByFechaDesc(Long idUnidadDeVenta);
}
