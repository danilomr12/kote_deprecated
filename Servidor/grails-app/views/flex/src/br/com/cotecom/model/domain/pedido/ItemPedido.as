package br.com.cotecom.model.domain.pedido {

[Bindable]
[RemoteClass(alias="br.com.cotecom.domain.dto.pedido.ItemPedidoDTO")]
public class ItemPedido {

    public var id:int;
    public var produtoId:int;
    public var qtdPedida:int;
    public var precoEmbalagem:Number;
    public var embalagem:String;
    public var descricao:String;
    public var obs:String;

    public function ItemPedido() {
    }

    public function get qtdEmbalagem():int{
        if(this.embalagem.length==10){
            return Number(this.embalagem.substring(3,7))
        }
        return 0;
    }

    public function get precoUnitario():Number {
        return this.precoEmbalagem/this.qtdEmbalagem;
    }
}
}
