package br.com.cotecom.model.domain.pedido {
import br.com.cotecom.view.util.DinheiroUtil;

[Bindable]
[RemoteClass(alias="br.com.cotecom.domain.dto.pedido.PedidoDTO")]
public class Pedido {

    public var id:*;
    public var respostaId:*;
    public var totalPedido:Number;
    public var totalItens:int;
    public var cotacaoId:*;
    public var representanteId:*;
    public var dataCriacao:Date;
    public var faturado:Boolean;


    public function Pedido(id:* = null, respostaId:* = null, totalPedido:Number = 0, totalItens:int = 0,
                           cotacaoId:* = null, representanteId:* = null, dataCriacao:Date = null, faturado:Boolean = false) {
        this.id = id;
        this.respostaId = respostaId;
        this.totalPedido = totalPedido;
        this.totalItens = totalItens;
        this.cotacaoId = cotacaoId;
        this.representanteId = representanteId;
        this.dataCriacao = dataCriacao;
        this.faturado = faturado;
    }

    public function get totalPedidoFormatadoReal():String {
        return DinheiroUtil.formatAsReal(this.totalPedido);
    }
}
}
