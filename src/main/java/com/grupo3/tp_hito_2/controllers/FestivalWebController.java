package com.grupo3.tp_hito_2.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.grupo3.tp_hito_2.services.IFestivalService;
import com.grupo3.tp_hito_2.services.IUnidadDeVentaService;

@Controller
public class FestivalWebController {

    private final IFestivalService festivalService;
    private final IUnidadDeVentaService unidadDeVentaService;

    public FestivalWebController(
            IFestivalService festivalService,
            IUnidadDeVentaService unidadDeVentaService) {
        this.festivalService = festivalService;
        this.unidadDeVentaService = unidadDeVentaService;
    }

    @GetMapping("/festivales")
    public String listarFestivales(Model model) {
        model.addAttribute("festivales", festivalService.getAll());
        return "festivales";
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
}