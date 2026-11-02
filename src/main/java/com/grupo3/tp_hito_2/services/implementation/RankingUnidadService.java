package com.grupo3.tp_hito_2.services.implementation;

import com.grupo3.tp_hito_2.dtos.RankingUnidadDTO;
import com.grupo3.tp_hito_2.dtos.ReporteVentaDTO;
import com.grupo3.tp_hito_2.entities.Festival;
import com.grupo3.tp_hito_2.entities.Pedido;
import com.grupo3.tp_hito_2.entities.Pedido_plato;
import com.grupo3.tp_hito_2.entities.UnidadDeVenta;
import com.grupo3.tp_hito_2.repositories.IFestivalRepository;
import com.grupo3.tp_hito_2.repositories.IUnidadDeVentaRepository;
import com.grupo3.tp_hito_2.services.IPedidoPlatoService;
import com.grupo3.tp_hito_2.services.IPedidoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service("rankingUnidadService")
public class RankingUnidadService {

    private final IUnidadDeVentaRepository unidadDeVentaRepository;
    private final IFestivalRepository festivalRepository;
    private final IPedidoService pedidoService;
    private final IPedidoPlatoService pedidoPlatoService;

    public RankingUnidadService(
            IUnidadDeVentaRepository unidadDeVentaRepository,
            IFestivalRepository festivalRepository,
            IPedidoService pedidoService,
            IPedidoPlatoService pedidoPlatoService) {

        this.unidadDeVentaRepository = unidadDeVentaRepository;
        this.festivalRepository = festivalRepository;
        this.pedidoService = pedidoService;
        this.pedidoPlatoService = pedidoPlatoService;
    }

    @Transactional(readOnly = true)
    public List<RankingUnidadDTO> generarRankingRecaudacion(Long idFestival) {

        festivalRepository.findById(idFestival)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el festival con ID: " + idFestival));

        List<UnidadDeVenta> unidades =
                unidadDeVentaRepository.findByFestival_IdFestival(idFestival);

        return unidades.stream()
                .map(unidad -> {

                    List<Pedido> pedidos =
                            pedidoService.findByUnidadDeVenta(
                                    unidad.getIdUnidadDeVenta());

                    double recaudacion = pedidos.stream()
                            .flatMap(pedido ->
                                    pedidoPlatoService.findByPedido(
                                            pedido.getIdPedido()).stream())
                            .mapToDouble(Pedido_plato::getSubTotal)
                            .sum();

                    return new RankingUnidadDTO(
                            unidad.getIdUnidadDeVenta(),
                            unidad.getNombreComercial(),
                            unidad.getCodigoUnico(),
                            String.valueOf(unidad.getTipoUnidad()),
                            recaudacion
                    );
                })
                .sorted(Comparator.comparingDouble(
                        RankingUnidadDTO::getRecaudacion).reversed())
                .toList();
    }

    @Transactional(readOnly = true)
    public ReporteVentaDTO generarReporteRecaudacion(Long idFestival) {

        Festival festival = festivalRepository.findById(idFestival)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el festival con ID: " + idFestival));

        List<RankingUnidadDTO> ranking =
                generarRankingRecaudacion(idFestival);

        double recaudacionTotal = ranking.stream()
                .mapToDouble(RankingUnidadDTO::getRecaudacion)
                .sum();

        return new ReporteVentaDTO(
                festival.getIdFestival(),
                festival.getNombre(),
                recaudacionTotal
        );
    }
}