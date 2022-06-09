package br.com.cotecom.control.responders.cotacao {
import br.com.cotecom.control.responders.aplicacao.ServerResponder;
import br.com.cotecom.model.Session;
import br.com.cotecom.model.domain.tela.TelaCotacaoDTO;
import br.com.cotecom.model.services.comprador.GerenciadorCotacao;
import br.com.cotecom.view.util.Icons;

import mx.rpc.events.ResultEvent;

public class SaveCotacaoResponder extends ServerResponder{

    public var gerenciadorCotacao:GerenciadorCotacao = GerenciadorCotacao.getInstance();
    private var model:Session = Session.getInstance();
    public var target:TelaCotacaoDTO;

    public function SaveCotacaoResponder(target:TelaCotacaoDTO) {
        super();
        this.target = target;
    }

    override public function result(data:Object):void {
        super.resetCommunication();
        if (hasErrors(data)) return;

        var evento:ResultEvent = data as ResultEvent;
        if (data == null) {
            fault(data);
        }
        var telaNovaCotacaoDTO:TelaCotacaoDTO = evento.result as TelaCotacaoDTO;

        if (telaNovaCotacaoDTO != null) {
            gerenciadorCotacao.telaCotacaoDTO = telaNovaCotacaoDTO;
            model.messageHandler.showTextMessage("Cotação", "Cotação salva com sucesso", Icons.SUCESSFUL_24);
        } else {
            fault(data);
        }
    }

}
}
