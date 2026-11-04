package com.grupo3.tp_hito_2.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// Cierre de caja diario de una unidad de venta, realizado por un cajero.
// Una unidad solo puede cerrar su caja una vez por día (restricción única).
@Entity
@Getter @Setter @NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "cierre_caja",
       uniqueConstraints = @UniqueConstraint(columnNames = {"unidad_de_venta_id", "fecha"}))
public class CierreCaja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private Long idCierreCaja;

    @Column(nullable = false)
    @ToString.Include
    private LocalDate fecha;

    private LocalDateTime fechaHoraCierre;

    @ToString.Include
    private double totalRecaudado;

    private int cantidadPedidos;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cajero_id", nullable = false)
    private Cajero cajero;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "unidad_de_venta_id", nullable = false)
    private UnidadDeVenta unidadDeVenta;

    public CierreCaja(Cajero cajero, UnidadDeVenta unidadDeVenta, LocalDate fecha,
                      double totalRecaudado, int cantidadPedidos) {
        this.cajero = cajero;
        this.unidadDeVenta = unidadDeVenta;
        this.fecha = fecha;
        this.fechaHoraCierre = LocalDateTime.now();
        this.totalRecaudado = totalRecaudado;
        this.cantidadPedidos = cantidadPedidos;
    }
}
