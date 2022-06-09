package br.com.cotecom.control.responders.cotacao {
import br.com.cotecom.control.events.aplicacao.ShowViewCaixaDeEntradaEvent;
import br.com.cotecom.control.responders.aplicacao.ServerResponder;
import br.com.cotecom.model.Session;
import br.com.cotecom.model.domain.cotacao.CotacaoDTO;
import br.com.cotecom.model.services.comprador.GerenciadorCotacao;
import br.com.cotecom.view.util.Icons;

import mx.rpc.events.ResultEvent;

public class CanceleCotacaoResponder extends ServerResponder{

    public var model:Session = Session.getInstance();
    public var gerenciadorCotacao:GerenciadorCotacao = GerenciadorCotacao.getInstance();
    public var target:CotacaoDTO;

    public function CanceleCotacaoResponder(target:CotacaoDTO) {
        super();
        this.target = target;
    }

    override public function result(event:Object):void {
        super.resetCommunication();
        if (hasErrors(event)) return;

        var evento:ResultEvent = event as ResultEvent;
        if (evento == null || !evento.result) {
            fault(event);
        } else {
            gerenciadorCotacao.limpeTelaNovaCotacao();
            new ShowViewCaixaDeEntradaEvent().dispatch();
            model.messageHandler.showTextMessage("Cotacao", "Cotação cancelada", Icons.INFORMATION);
        }
    }
}
}
