package com.grupo3.tp_hito_2.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class PedidoPlatoDTO {
    private Long idPedidoPlato;
    private Long idPedido;
    private Long idPlato;
    private double subTotal;

    public PedidoPlatoDTO(Long idPedidoPlato, Long idPedido, Long idPlato, double subTotal) {
        this.idPedidoPlato = idPedidoPlato;
        this.idPedido = idPedido;
        this.idPlato = idPlato;
        this.subTotal = subTotal;
    }
}
