package br.com.cotecom.control.responders.cotacao {
import br.com.cotecom.control.responders.aplicacao.ServerResponder;
import br.com.cotecom.model.Session;
import br.com.cotecom.model.domain.cotacao.ItemCotacaoDTO;
import br.com.cotecom.model.services.comprador.GerenciadorCotacao;
import br.com.cotecom.view.util.Icons;

import mx.collections.ArrayCollection;
import mx.rpc.events.ResultEvent;

public class RemovaItemCotacaoResponder extends ServerResponder{

    private var model:Session = Session.getInstance();
    private var gerenciadorCotacao:GerenciadorCotacao = GerenciadorCotacao.getInstance();

    public function RemovaItemCotacaoResponder() {
        super();
    }

    override public function result(data:Object):void {
        super.resetCommunication();
        if (hasErrors(data)) return;

        var evento:ResultEvent = data as ResultEvent;
        if(evento==null || !evento.result){
            fault(evento);
        }
        var itensCotacaoDTO:ArrayCollection = gerenciadorCotacao.telaCotacaoDTO.itensCotacaoDTO;
        var idItemCotacaoARemover:int = evento.result as int;
        var itemIndex:int = -1;
        for each(var item:ItemCotacaoDTO in itensCotacaoDTO){
            if(item.id == idItemCotacaoARemover)              {
                itemIndex = itensCotacaoDTO.getItemIndex(item);
                break;
            }
        }
        var itemRemovido:ItemCotacaoDTO = itensCotacaoDTO.removeItemAt(itemIndex) as ItemCotacaoDTO;
        model.messageHandler.showTextMessage("Cotacao", "Produto: "+ itemRemovido.descricao + " " + itemRemovido.embalagem +
                " removido da cotação", Icons.DELETE_24)
    }

}
}
