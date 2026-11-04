package com.grupo3.tp_hito_2.services.implementation;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import com.grupo3.tp_hito_2.repositories.IFestivalRepository;
import com.grupo3.tp_hito_2.repositories.IPedidoRepository;
import com.grupo3.tp_hito_2.repositories.IPlatoRepository;
import com.grupo3.tp_hito_2.repositories.IUnidadDeVentaRepository;
import com.grupo3.tp_hito_2.services.IPlatoService;

import org.springframework.transaction.annotation.Transactional;

import com.grupo3.tp_hito_2.dtos.PlatoDTO;
import com.grupo3.tp_hito_2.entities.Festival;
import com.grupo3.tp_hito_2.entities.Plato;
import com.grupo3.tp_hito_2.entities.UnidadDeVenta;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service("platoService")
public class PlatoService implements IPlatoService {
    private static final Logger logger = LoggerFactory.getLogger(PlatoService.class);

    private final IPlatoRepository platoRepository;
    private final IUnidadDeVentaRepository unidadDeVentaRepository;
    private final IFestivalRepository festivalRepository;
    private final IPedidoRepository pedidoRepository;
    private final ModelMapper modelMapper;

    public PlatoService(IPlatoRepository platoRepository, IUnidadDeVentaRepository unidadDeVentaRepository,
            IFestivalRepository festivalRepository, IPedidoRepository pedidoRepository,ModelMapper modelMapper) {
        this.platoRepository = platoRepository;
        this.unidadDeVentaRepository = unidadDeVentaRepository;
        this.festivalRepository=festivalRepository;
        this.pedidoRepository=pedidoRepository;
        this.modelMapper = modelMapper;
    }

    private UnidadDeVenta unidadDelResponsable(String username) {
        return unidadDeVentaRepository.findByResponsableUsername(username)
                .orElseThrow(
                        () -> new IllegalArgumentException("No se encontró una unidad de venta para el responsable"));
    }

    @Override
    public List<Plato> getAll() {
        return platoRepository.findAll();
    }

    @Override
    public Optional<Plato> findById(Long id) {
        return platoRepository.findById(id);
    }

    @Override
    public Plato insertOrUpdate(Plato plato) {
        return platoRepository.save(plato);
    }

    @Override
    public boolean remove(Long id) {
        try {
            platoRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException | DataIntegrityViolationException e) {
            logger.warn("No se pudo eliminar el plato con id {}", id, e);
            return false;
        } catch (DataAccessException e) {
            logger.error("Error de base de datos al eliminar plato con id {}", id, e);
            return false;
        }
    }

    @Override
    public Optional<Plato> findByNombreAndUnidadDeVenta(String nombre, UnidadDeVenta unidadDeVenta) {
        return platoRepository.findByNombreAndUnidadDeVenta(nombre, unidadDeVenta);
    }

    @Override
    public PlatoDTO convertirADTO(Plato plato) {
        return modelMapper.map(plato, PlatoDTO.class);
    }

    @Override
    @Transactional
    public Plato registrarPlato(PlatoDTO dto, String username) {
        UnidadDeVenta unidad = unidadDelResponsable(username);
        String nombre = dto.getNombre().trim();
        boolean existe = platoRepository.findByUnidadDeVenta(unidad)
                .stream()
                .anyMatch(plato -> plato.getNombre().equalsIgnoreCase(nombre));
        if (existe) {
            throw new IllegalArgumentException("Ya existe un plato con ese nombre en tu unidad de venta");
        }
        Plato plato = new Plato(nombre, dto.getPrecio(), dto.getCostoDeProduccion());
        plato.setUnidadDeVenta(unidad);
        return platoRepository.save(plato);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlatoDTO> listarPlatosResponsable(String username) {
        UnidadDeVenta unidad = unidadDelResponsable(username);

        return platoRepository.findByUnidadDeVenta(unidad).stream()
                .map(plato -> {
                    PlatoDTO dto = modelMapper.map(plato, PlatoDTO.class);

                    dto.setIdUnidadDeVenta(
                            plato.getUnidadDeVenta().getIdUnidadDeVenta());

                    return dto;
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PlatoDTO obtenerPlatoEstrella(Long idUnidad, Long idFestival){
        Festival festival = festivalRepository.findById(idFestival).orElseThrow(()
            -> new RuntimeException("Festival no encontrado"));
        List<Plato> platos = pedidoRepository.buscarPlatosOrdenadosPorCantidad(idUnidad, idFestival, festival.getFechaInicio(), festival.getFechaFin());
        if(platos.isEmpty()){
            return null;
        }
        return modelMapper.map(platos.get(0), PlatoDTO.class);
    }

}
