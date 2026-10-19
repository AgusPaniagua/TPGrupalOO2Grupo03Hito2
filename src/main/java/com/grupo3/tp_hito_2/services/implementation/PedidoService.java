package com.grupo3.tp_hito_2.services.implementation;

import com.grupo3.tp_hito_2.dtos.PedidoDTO;
import com.grupo3.tp_hito_2.entities.Pedido;
import com.grupo3.tp_hito_2.repositories.IPedidoRepository;
import com.grupo3.tp_hito_2.services.IPedidoService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;


@Service("pedidoService")
public class PedidoService implements IPedidoService {

    private static final Logger logger =
            LoggerFactory.getLogger(PedidoService.class);

    private final IPedidoRepository pedidoRepository;
    private final ModelMapper modelMapper;

    public PedidoService(IPedidoRepository pedidoRepository,
                         ModelMapper modelMapper) {
        this.pedidoRepository = pedidoRepository;
        this.modelMapper = modelMapper;
    }

    public List<Pedido> getAll() {
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> findById(Long id) {
        return pedidoRepository.findById(id);
    }

    public List<Pedido> findByUnidadDeVenta(Long idUnidadDeVenta) {
        return pedidoRepository.findByUnidadDeVenta_IdUnidadDeVenta(idUnidadDeVenta);
    }

    public Pedido insertOrUpdate(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public boolean remove(Long id) {
        try {
            pedidoRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException |
                 DataIntegrityViolationException e) {
            logger.warn("No se pudo eliminar el pedido con id {}", id, e
            );
            return false;
        }
    }

    public PedidoDTO convertirADTO(Pedido pedido) {
        return modelMapper.map(pedido, PedidoDTO.class);
    }
}
