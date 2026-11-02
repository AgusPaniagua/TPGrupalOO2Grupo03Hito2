package com.grupo3.tp_hito_2.controllers;

import com.grupo3.tp_hito_2.dtos.RankingUnidadDTO;
import com.grupo3.tp_hito_2.services.implementation.RankingUnidadService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.grupo3.tp_hito_2.services.IFestivalService;
import com.grupo3.tp_hito_2.services.IUnidadDeVentaService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class FestivalWebController {

    private final IFestivalService festivalService;
    private final IUnidadDeVentaService unidadDeVentaService;
    private final RankingUnidadService rankingUnidadService;
    public FestivalWebController(
            IFestivalService festivalService,
            IUnidadDeVentaService unidadDeVentaService,
            RankingUnidadService rankingUnidadService) {

        this.festivalService = festivalService;
        this.unidadDeVentaService = unidadDeVentaService;
        this.rankingUnidadService = rankingUnidadService;
    }

    @GetMapping("/festivales")
    public String listarFestivales(Model model) {
        model.addAttribute("festivales", festivalService.getAll());
        return "festivales";
    }
    @GetMapping("/unidades-de-venta")
    public String seleccionarFestivalParaUnidades(Model model) {
        model.addAttribute("festivales", festivalService.getAll());
        return "seleccionar-festival-unidades";
    }
    @GetMapping("/ranking-recaudacion")
    public String seleccionarFestivalParaRanking(Model model) {
        model.addAttribute("festivales", festivalService.getAll());
        return "seleccionar-festival-ranking";
    }
    @GetMapping("/ranking-recaudacion/consultar")
    public String consultarRankingRecaudacion(

            @RequestParam Long idFestival, Model model) {

        var festival = festivalService.getAll().stream()
                .filter(f -> idFestival.equals(f.getIdFestival()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe el festival con ID: " + idFestival));

        List<RankingUnidadDTO> ranking =
                rankingUnidadService.generarRankingRecaudacion(idFestival);

        model.addAttribute("festival", festival);
        model.addAttribute("ranking", ranking);
        model.addAttribute("festivales", festivalService.getAll());
        return "ranking-recaudacion";
    }
    @GetMapping("/festivales/{idFestival}/unidades-de-venta")
    public String verUnidadesDeVenta(
            @PathVariable Long idFestival,
            Model model) {

        var festival = festivalService.getAll().stream()
                .filter(f -> idFestival.equals(f.getIdFestival()))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No existe el festival con ID: " + idFestival));

        model.addAttribute("festival", festival);
        var unidades = unidadDeVentaService.traerPorFestival(idFestival);

        System.out.println("Festival seleccionado: " + idFestival);
        System.out.println("Cantidad de unidades: " + unidades.size());

        model.addAttribute("unidadesDeVenta", unidades);
        return "unidades-de-venta";
    }
    @GetMapping("/festivales/{idFestival}/ranking-recaudacion")
    public String verRankingRecaudacion(
            @PathVariable Long idFestival,
            Model model) {

        var festival = festivalService.getAll().stream().filter(f -> idFestival.equals(f.getIdFestival())).findFirst().orElseThrow(() ->
                        new IllegalArgumentException(
                                "No existe el festival con ID: " + idFestival));

        List<RankingUnidadDTO> ranking =
                rankingUnidadService.generarRankingRecaudacion(idFestival);

        model.addAttribute("festival", festival);
        model.addAttribute("ranking", ranking);

        return "ranking-recaudacion";
    }
}