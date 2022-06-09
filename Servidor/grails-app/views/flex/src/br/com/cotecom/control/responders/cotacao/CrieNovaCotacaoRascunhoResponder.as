package br.com.cotecom.control.responders.cotacao {
import br.com.cotecom.control.responders.aplicacao.ServerResponder;
import br.com.cotecom.model.domain.cotacao.CotacaoDTO;
import br.com.cotecom.model.services.comprador.GerenciadorCotacao;

import mx.rpc.events.ResultEvent;

public class CrieNovaCotacaoRascunhoResponder extends ServerResponder{

    public function CrieNovaCotacaoRascunhoResponder() {
        super();
    }

    override public function result(data:Object):void {
        super.resetCommunication();
        if (hasErrors(data)) return;

        var evento:ResultEvent = data as ResultEvent;
        if(data==null){
            fault(data);
        }
        var gerenciadorCotacao:GerenciadorCotacao = GerenciadorCotacao.getInstance();
        gerenciadorCotacao.cotacaoSelecionada = evento.result as CotacaoDTO;
        gerenciadorCotacao.telaCotacaoDTO.cotacaoDTO = evento.result as CotacaoDTO;
    }

}
}
