package com.grupo3.tp_hito_2.services.implementation;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.grupo3.tp_hito_2.dtos.PedidoPlatoDTO;
import com.grupo3.tp_hito_2.entities.Pedido_plato;
import com.grupo3.tp_hito_2.repositories.IPedidoPlatoRepository;
import com.grupo3.tp_hito_2.services.IPedidoPlatoService;

@Service("pedidoPlatoService")
public class PedidoPlatoService implements IPedidoPlatoService {

    private static final Logger logger = LoggerFactory.getLogger(PedidoPlatoService.class);

    private final IPedidoPlatoRepository pedidoPlatoRepository;
    private final ModelMapper modelMapper;

    public PedidoPlatoService(IPedidoPlatoRepository pedidoPlatoRepository, ModelMapper modelMapper) {
        this.pedidoPlatoRepository = pedidoPlatoRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<Pedido_plato> getAll() {
        return pedidoPlatoRepository.findAll();
    }

    @Override
    public Optional<Pedido_plato> findById(Long id) {
        return pedidoPlatoRepository.findById(id);
    }

    @Override
    public List<Pedido_plato> findByPedido(Long idPedido) {
        return pedidoPlatoRepository.findByPedido_IdPedido(idPedido);
    }

    @Override
    public Pedido_plato insertOrUpdate(Pedido_plato pedidoPlato) {
        return pedidoPlatoRepository.save(pedidoPlato);
    }

    @Override
    public boolean remove(Long id) {
        try {
            pedidoPlatoRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException | DataIntegrityViolationException e) {
            logger.warn("No se pudo eliminar el detalle de pedido con id {}", id, e);
            return false;
        }
    }

    @Override
    public PedidoPlatoDTO convertirADTO(Pedido_plato pedidoPlato) {
        return modelMapper.map(pedidoPlato, PedidoPlatoDTO.class);
    }
}
