package com.grupo3.tp_hito_2.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// Detalle del pedido: une un Pedido con un Plato (según el diagrama de clases).
// Se llama Pedido_plato porque así lo referencia Pedido (List<Pedido_plato> pedidosPlatos).
@Entity
@Getter @Setter @NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class Pedido_plato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private Long idPedidoPlato;

    @ToString.Include
    private double subTotal;

    // El nombre del campo debe ser "pedido": Pedido lo referencia con mappedBy = "pedido"
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPedido")
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPlato")
    private Plato plato;

    public Pedido_plato(Pedido pedido, Plato plato, double subTotal) {
        this.pedido = pedido;
        this.plato = plato;
        this.subTotal = subTotal;
    }
}
