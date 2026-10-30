package com.grupo3.tp_hito_2.controllers;

import com.grupo3.tp_hito_2.entities.Costo;
import com.grupo3.tp_hito_2.entities.Festival;
import com.grupo3.tp_hito_2.repositories.IFestivalRepository;
import com.grupo3.tp_hito_2.repositories.ICostoRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final IFestivalRepository festivalRepository;
    private final ICostoRepository costoRepository;

    public DataInitializer(IFestivalRepository festivalRepository,
                           ICostoRepository costoRepository) {
        this.festivalRepository = festivalRepository;
        this.costoRepository = costoRepository;
    }

    @Override
    public void run(String... args) {

        if (festivalRepository.findByNombre("Festival Primavera").isEmpty()) {
        crearFestival("Festival Primavera",
                LocalDate.of(2026, 11, 10),
                LocalDate.of(2026, 11, 10),
                LocalDate.of(2026, 11, 15),
                "1500", "800", "500", "100000");
    }

    if (festivalRepository.findByNombre("Festival de Rock").isEmpty()) {
        crearFestival("Festival de Rock",
                LocalDate.of(2026, 12, 5),
                LocalDate.of(2026, 12, 5),
                LocalDate.of(2026, 12, 10),
                "2000", "1200", "700", "150000");
    }

    if (festivalRepository.findByNombre("Festival de Jazz").isEmpty()) {
        crearFestival("Festival de Jazz",
                LocalDate.of(2027, 1, 15),
                LocalDate.of(2027, 1, 15),
                LocalDate.of(2027, 1, 20),
                "1000", "600", "300", "80000");
    }

    if (festivalRepository.findByNombre("Festival de Verano").isEmpty()) {
        crearFestival("Festival de Verano",
                LocalDate.of(2027, 2, 10),
                LocalDate.of(2027, 2, 10),
                LocalDate.of(2027, 2, 18),
                "2500", "1500", "900", "200000");
    }
    }

    private void crearFestival(
            String nombre,
            LocalDate temporada,
            LocalDate fechaInicio,
            LocalDate fechaFin,
            String superficie,
            String montaje,
            String electricidad,
            String sueldoBase) {

        Festival festival = new Festival(
                nombre,
                temporada,
                fechaInicio,
                fechaFin,
                null
        );

        festivalRepository.save(festival);

        Costo costo = new Costo(
                new BigDecimal(superficie),
                new BigDecimal(montaje),
                new BigDecimal(electricidad),
                new BigDecimal(sueldoBase),
                festival
        );

        costoRepository.save(costo);

        festival.setCosto(costo);
        festivalRepository.save(festival);
    }
}