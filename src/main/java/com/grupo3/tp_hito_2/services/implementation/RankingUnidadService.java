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
import java.math.BigDecimal;
import java.util.ArrayList;
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

        // Verificamos que exista el festival
        festivalRepository.findById(idFestival).orElseThrow(() -> new IllegalArgumentException
                ("No se encontró el festival con ID: " + idFestival));

        // Buscamos las unidades de venta del festival
        List<UnidadDeVenta> unidades = unidadDeVentaRepository.findByFestival_IdFestival(idFestival);

        // Creamos la lista donde guardaremos el ranking
        List<RankingUnidadDTO> ranking = new ArrayList<>();

        // Recorremos cada unidad de venta
        for (UnidadDeVenta unidad : unidades) {
            List<Pedido> pedidos =
                    pedidoService.findByUnidadDeVenta(unidad.getIdUnidadDeVenta());
            double recaudacion = 0;

            for (Pedido pedido : pedidos) {
                List<Pedido_plato> platos = pedidoPlatoService.findByPedido(pedido.getIdPedido());

                for (Pedido_plato plato : platos) {
                    recaudacion += plato.getSubTotal();
                }
            }
            // Creamos el DTO
            RankingUnidadDTO dto = new RankingUnidadDTO(
                    unidad.getIdUnidadDeVenta(),
                    unidad.getNombreComercial(),
                    unidad.getCodigoUnico(),
                    String.valueOf(unidad.getTipoUnidad()),
                    recaudacion
            );

            ranking.add(dto);
        }

        // Ordenamos las unidades de mayor a menor recaudación
        ranking.sort(Comparator.comparingDouble(
                RankingUnidadDTO::getRecaudacion).reversed());

        return ranking;
    }


    @Transactional(readOnly = true)
    public ReporteVentaDTO generarReporteRecaudacion(Long idFestival) {
        double recaudacionTotal = 0;
        //busca el festival
        Festival festival = festivalRepository.findById(idFestival)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el festival con ID: " + idFestival));
        //obtine el ranking de la unidad de venta
        List<RankingUnidadDTO> ranking = generarRankingRecaudacion(idFestival);
        //suma la recaudacion
        for (RankingUnidadDTO unidad : ranking) {
            recaudacionTotal += unidad.getRecaudacion();
        }
        return new ReporteVentaDTO(festival.getIdFestival(), festival.getNombre(), recaudacionTotal);
    }
}