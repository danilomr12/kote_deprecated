package br.com.cotecom.control.responders.cotacao {
import br.com.cotecom.control.errors.ServerError;
import br.com.cotecom.control.message.MessageIcons;
import br.com.cotecom.control.responders.aplicacao.ServerResponder;
import br.com.cotecom.model.Session;
import br.com.cotecom.model.domain.cotacao.ItemCotacaoDTO;
import br.com.cotecom.model.services.comprador.GerenciadorCotacao;

import mx.collections.ArrayCollection;
import mx.rpc.events.ResultEvent;

public class AdicioneItemCotacaoResponder extends ServerResponder{

    public function AdicioneItemCotacaoResponder() {
        super();
    }


    override public function result(data:Object):void {
        super.resetCommunication();
        if (hasErrors(data)) return;

        var evento:ResultEvent = data as ResultEvent;
        var listaItensCotacao:ArrayCollection = evento.result as ArrayCollection;
        if(listaItensCotacao.getItemAt(0) is ServerError){
            fault(listaItensCotacao)
        }else{
            var totalItens:int = 0;
            for each(var item:* in listaItensCotacao){
                if(item is ItemCotacaoDTO){
                    GerenciadorCotacao.getInstance().telaCotacaoDTO.itensCotacaoDTO.addItem(item);
                    totalItens++
                }
            }
            Session.getInstance().messageHandler.showTextMessage("Cotacao", totalItens + " Itens adicionados a Cotação",
                    MessageIcons.SUCESSFUL);
        }
    }

}
}
