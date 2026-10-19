package com.grupo3.tp_hito_2.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @NoArgsConstructor
@ToString (onlyExplicitlyIncluded = true)
@Inheritance(strategy = InheritanceType.JOINED)
public class Festival {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private long idFestival;
    @ToString.Include
    private String nombre;
    @ToString.Include
    private LocalDate temporada;
    @ToString.Include
    private  LocalDate fechaInicio;
    @ToString.Include
    private LocalDate fechaFin;
    @OneToOne
    @JoinColumn(name = "idCosto")
    private Costo costo;
    @OneToMany(mappedBy = "festival")
    private List<UnidadDeVenta> unidadesDeVenta = new ArrayList<>();

    public Festival(String nombre, LocalDate temporada, LocalDate fechaInicio, LocalDate fechaFin,
                    Costo costo) {
        this.nombre = nombre;
        this.temporada = temporada;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.costo = costo;
    }

    public Festival(String nombre, LocalDate temporada,LocalDate fechaInicio, LocalDate fechaFin) {
        this.nombre = nombre;
        this.temporada = temporada;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }
}
