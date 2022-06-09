package br.com.cotecom.control.responders.resposta {
import br.com.cotecom.control.responders.aplicacao.ServerResponder;
import br.com.cotecom.model.domain.pedido.TelaPedido;
import br.com.cotecom.model.services.resposta.GerenciadorResposta;

import mx.collections.ArrayCollection;

import mx.rpc.events.ResultEvent;

public class LoadPedidoResponder extends ServerResponder {

    public var gerenciador:GerenciadorResposta = GerenciadorResposta.getInstance();

    public function LoadPedidoResponder() {
        super();
    }

    override public function result(event:Object):void {
        super.resetCommunication();
        if (hasErrors(event)) return;

        var evento:ResultEvent = event as ResultEvent;
        if (event.result is ArrayCollection){
            fault(event.result);
            return;
        }
        gerenciador.telaPedidoDTO = evento.result as TelaPedido;

    }
}
}