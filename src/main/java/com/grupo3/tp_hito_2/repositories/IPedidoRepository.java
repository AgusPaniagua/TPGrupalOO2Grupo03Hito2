package com.grupo3.tp_hito_2.repositories;

import com.grupo3.tp_hito_2.entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IPedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByUnidadDeVenta_IdUnidadDeVenta(Long idUnidadDeVenta);
//RESNTABILIDAD NETA POR FECHA
    List<Pedido> findByUnidadDeVenta_IdUnidadDeVentaAndFechaDeTransaccionBetween
    (Long idUnidadDeVenta, LocalDate fechaDesde, LocalDate fechaHasta);

    long countByUnidadDeVenta_IdUnidadDeVentaAndFechaDeTransaccion(Long idUnidadDeVenta, LocalDate fecha);
}
