package br.com.cotecom.control.responders.cotacao {
import br.com.cotecom.control.events.aplicacao.ShowViewCaixaDeEntradaEvent;
import br.com.cotecom.control.responders.aplicacao.ServerResponder;
import br.com.cotecom.model.Session;
import br.com.cotecom.model.domain.tela.TelaCotacaoDTO;
import br.com.cotecom.model.services.comprador.GerenciadorCotacao;
import br.com.cotecom.view.util.Icons;

import mx.rpc.events.FaultEvent;
import mx.rpc.events.ResultEvent;

public class SendCotacaoResponder extends ServerResponder {

    private var model:Session = Session.getInstance();
    public var gerenciadorCotacao:GerenciadorCotacao = GerenciadorCotacao.getInstance();
    public var target:TelaCotacaoDTO;

    public function SendCotacaoResponder(target:TelaCotacaoDTO) {
        super();
        this.target = target;
    }

    override public function result(data:Object):void {
        super.resetCommunication();
        if (hasErrors(data)) return;

        var evento:ResultEvent = data as ResultEvent;
        var resultado:Boolean = evento.result as Boolean;
        if (resultado) {
            model.messageHandler.showTextMessage("Cotação", "Cotacao enviada com sucesso", Icons.SUCESSFUL_24);
            new ShowViewCaixaDeEntradaEvent().dispatch();
            gerenciadorCotacao.limpeTelaNovaCotacao();
        } else
            fault(data);

    }

    override public function fault(event:Object):void {
        gerenciadorCotacao.telaCotacaoDTO.representantesId.removeAll();
        resetCommunication();
        trace(event.valueOf());
        Session.getInstance().errorHandler.showError(event as FaultEvent);
    }

}
}