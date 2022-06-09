package br.com.cotecom.control.events.cotacao{

import br.com.cotecom.model.domain.dtos.AnaliseCotacaoDTO;

import com.adobe.cairngorm.control.CairngormEvent;

import flash.events.Event;

public class GeraPedidosEvent extends CairngormEvent {

    public static const GERAR_PEDIDOS : String = "GerarPedidosEvent";

    public var analise:AnaliseCotacaoDTO;

    public function GeraPedidosEvent(analise:AnaliseCotacaoDTO) {
        this.analise = analise;
        super(GERAR_PEDIDOS,true);
    }

    public override function clone():Event {
        return new GeraPedidosEvent(analise);
    }
}
}
