```java
package com.grupo3.tp_hito_2.repositories;

import com.grupo3.tp_hito_2.entities.Pedido;
import com.grupo3.tp_hito_2.entities.Plato;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IPedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByUnidadDeVenta_IdUnidadDeVenta(Long idUnidadDeVenta);

    // RENTABILIDAD NETA POR FECHA
    List<Pedido> findByUnidadDeVenta_IdUnidadDeVentaAndFechaDeTransaccionBetween(
            Long idUnidadDeVenta, LocalDate fechaDesde, LocalDate fechaHasta);

    long countByUnidadDeVenta_IdUnidadDeVentaAndFechaDeTransaccion(
            Long idUnidadDeVenta, LocalDate fecha);

    // PLATO ESTRELLA
    @Query("""
            SELECT pp.plato
            FROM Pedido_plato pp
            WHERE pp.pedido.unidadDeVenta.idUnidadDeVenta = :idUnidad
              AND pp.pedido.unidadDeVenta.festival.idFestival = :idFestival
              AND pp.pedido.fechaDeTransaccion BETWEEN :fechaInicio AND :fechaFin
            GROUP BY pp.plato
            ORDER BY SUM(pp.cantidad) DESC, pp.plato.idPlato ASC
            """)
    List<Plato> buscarPlatosOrdenadosPorCantidad(
            @Param("idUnidad") Long idUnidad,
            @Param("idFestival") Long idFestival,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);
}
```