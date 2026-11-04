package com.grupo3.tp_hito_2.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.grupo3.tp_hito_2.entities.Pedido_plato;

@Repository("pedidoPlatoRepository")
public interface IPedidoPlatoRepository extends JpaRepository<Pedido_plato, Long> {

    List<Pedido_plato> findByPedido_IdPedido(Long idPedido);

    // Total vendido por una unidad en un día (devuelve null si no hubo pedidos)
    @Query("SELECT SUM(pp.subTotal) FROM Pedido_plato pp "
         + "WHERE pp.pedido.unidadDeVenta.idUnidadDeVenta = :idUnidad "
         + "AND pp.pedido.fechaDeTransaccion = :fecha")
    Double totalVendidoPorUnidadYFecha(@Param("idUnidad") Long idUnidad, @Param("fecha") LocalDate fecha);
}
