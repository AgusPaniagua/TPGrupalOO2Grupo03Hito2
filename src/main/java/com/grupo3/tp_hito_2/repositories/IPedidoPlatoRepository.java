package com.grupo3.tp_hito_2.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grupo3.tp_hito_2.entities.Pedido_plato;

@Repository("pedidoPlatoRepository")
public interface IPedidoPlatoRepository extends JpaRepository<Pedido_plato, Long> {

    List<Pedido_plato> findByPedido_IdPedido(Long idPedido);
}
