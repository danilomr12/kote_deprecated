package br.com.cotecom.control.responders.resposta {
import br.com.cotecom.control.responders.aplicacao.ServerResponder;
import br.com.cotecom.model.Session;
import br.com.cotecom.model.domain.pedido.TelaPedido;
import br.com.cotecom.model.services.resposta.GerenciadorResposta;
import br.com.cotecom.view.util.Icons;
import mx.rpc.events.ResultEvent;

public class FaturePedidoResponder extends ServerResponder {

    public var gerenciador:GerenciadorResposta = GerenciadorResposta.getInstance();
    public var session:Session = Session.getInstance();

    public function FaturePedidoResponder() {
        super();
    }

    override public function result(event:Object):void {
        super.resetCommunication();
        if (hasErrors(event)) return;

        var evento:ResultEvent = event as ResultEvent;
        if (event.result == false){
            fault(event.result);
            return;
        }
        session.messageHandler.showTextMessage("Pedido", "Pedido faturado com sucesso",
                Icons.SUCESSFUL_24);
        gerenciador.telaPedidoDTO.pedido.faturado = true;
    }
}
}