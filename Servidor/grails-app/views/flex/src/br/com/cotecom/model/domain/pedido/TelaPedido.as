package br.com.cotecom.model.domain.pedido {
import br.com.cotecom.model.domain.dtos.usuarios.Usuario;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="br.com.cotecom.domain.dto.tela.TelaPedidoDTO")]
public class TelaPedido {

    public var pedido:Pedido;
    public var itensPedido:ArrayCollection;
    public var comprador:Usuario;


    public function TelaPedido() {
    }
}
}
