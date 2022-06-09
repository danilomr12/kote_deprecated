package br.com.cotecom.control.responders.cotacao {
import br.com.cotecom.control.responders.aplicacao.ServerResponder;
import br.com.cotecom.model.Session;
import br.com.cotecom.model.domain.cotacao.ItemCotacaoDTO;
import br.com.cotecom.model.domain.tela.TelaCotacaoDTO;
import br.com.cotecom.model.services.comprador.GerenciadorCotacao;
import br.com.cotecom.view.util.Icons;

import mx.rpc.events.ResultEvent;

public class SaveItemCotacaoResponder extends ServerResponder{

    public var gerenciadorCotacao:GerenciadorCotacao = GerenciadorCotacao.getInstance();
    private var model:Session = Session.getInstance();

    public function SaveItemCotacaoResponder() {
        super();
    }

    override public function result(data:Object):void {
        super.resetCommunication();
        if (hasErrors(data)) return;

        var evento:ResultEvent = data as ResultEvent;
        if (data == null) {
            fault(data);
        }
        var itemCotacaoDTO:ItemCotacaoDTO = evento.result as ItemCotacaoDTO;
        if (itemCotacaoDTO != null && itemCotacaoDTO.saved) {
            gerenciadorCotacao.atualizeItemCotacaoDTO(itemCotacaoDTO);
            model.messageHandler.showTextMessage("Cotação", "Produto atualizado com sucesso", Icons.SUCESSFUL_24);
        } else {
            fault(data);
        }
    }

}
}
