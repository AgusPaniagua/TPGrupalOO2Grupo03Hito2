package com.grupo3.tp_hito_2.services;

import java.util.List;
import java.util.Optional;

import com.grupo3.tp_hito_2.dtos.PedidoPlatoDTO;
import com.grupo3.tp_hito_2.entities.Pedido_plato;

public interface IPedidoPlatoService {

    List<Pedido_plato> getAll();

    Optional<Pedido_plato> findById(Long id);

    List<Pedido_plato> findByPedido(Long idPedido);

    Pedido_plato insertOrUpdate(Pedido_plato pedidoPlato);

    boolean remove(Long id);

    PedidoPlatoDTO convertirADTO(Pedido_plato pedidoPlato);
}
