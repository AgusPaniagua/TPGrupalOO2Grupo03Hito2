package com.grupo3.tp_hito_2.services;

import com.grupo3.tp_hito_2.dtos.PedidoDTO;
import com.grupo3.tp_hito_2.entities.Pedido;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IPedidoService {

    List<Pedido> getAll();

    Optional<Pedido> findById(Long id);

    List<Pedido> findByUnidadDeVenta(Long idUnidadDeVenta);

    Pedido insertOrUpdate(Pedido pedido);

    boolean remove(Long id);

    PedidoDTO convertirADTO(Pedido pedido);

    List<Pedido> findByUnidadDeVentaYFechas(
            Long idUnidadDeVenta,
            LocalDate fechaDesde,
            LocalDate fechaHasta
    );
}
