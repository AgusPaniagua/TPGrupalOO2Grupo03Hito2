package com.grupo3.tp_hito_2.entities;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPedido;

    private LocalDate fechaDeTransaccion;

    @ManyToOne
    @JoinColumn(name = "idUnidadDeVenta")
    private UnidadDeVenta unidadDeVenta;

    @OneToMany(mappedBy = "pedido")
    private List<Pedido_plato> pedidosPlatos = new ArrayList<>();

    public Pedido(LocalDate fechaDeTransaccion,UnidadDeVenta unidadDeVenta) {
        this.fechaDeTransaccion = fechaDeTransaccion;
        this.unidadDeVenta = unidadDeVenta;
    }
}

