package com.grupo3.tp_hito_2.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PedidoDTO {
    private long idPedido;
    private LocalDate fechaDeTransaccion;
    private UnidadDeVentaDTO unidadDeVenta;
    private List<PedidoPlatoDTO> pedidoPlato;

    public PedidoDTO (long idPedido,LocalDate fechaDeTransaccion,UnidadDeVentaDTO unidadDeVenta
            , List<PedidoPlatoDTO> pedidoPlatoParametro){
        this.setIdPedido(idPedido);
        this.fechaDeTransaccion = fechaDeTransaccion;
        this.unidadDeVenta=unidadDeVenta;
        this.pedidoPlato=pedidoPlatoParametro;
    }

}
