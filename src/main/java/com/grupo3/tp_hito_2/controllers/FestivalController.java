package com.grupo3.tp_hito_2.controllers;
import com.grupo3.tp_hito_2.services.implementation.RankingUnidadService;

import com.grupo3.tp_hito_2.dtos.ReporteVentaDTO;
import com.grupo3.tp_hito_2.entities.Festival;
import com.grupo3.tp_hito_2.services.IFestivalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/festivales")
public class FestivalController {

    private final IFestivalService festivalService;
    private final RankingUnidadService rankingUnidadService;

    public FestivalController(
            IFestivalService festivalService,
            RankingUnidadService rankingUnidadService) {

        this.festivalService = festivalService;
        this.rankingUnidadService = rankingUnidadService;
    }

    @GetMapping
    public List<Festival> getAll() {
        return festivalService.getAll();
    }

    @GetMapping("/{idFestival}/reporte-recaudacion")
    public ResponseEntity<ReporteVentaDTO> generarReporteRecaudacion(
            @PathVariable Long idFestival) {

        return ResponseEntity.ok(
                rankingUnidadService.generarReporteRecaudacion(idFestival)
        );
    }
}




