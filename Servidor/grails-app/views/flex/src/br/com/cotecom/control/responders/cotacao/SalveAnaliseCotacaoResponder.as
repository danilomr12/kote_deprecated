package br.com.cotecom.control.responders.cotacao {
import br.com.cotecom.control.responders.aplicacao.ServerResponder;
import br.com.cotecom.model.domain.cotacao.CotacaoDTO;
import br.com.cotecom.model.services.comprador.GerenciadorCotacao;
import br.com.cotecom.model.utils.EstadoCotacao;

import mx.collections.ArrayCollection;
import mx.rpc.events.ResultEvent;

public class SalveAnaliseCotacaoResponder extends ServerResponder {

    public var cotacaoSelecionada:CotacaoDTO;

    public function SalveAnaliseCotacaoResponder() {
        super();
        cotacaoSelecionada = GerenciadorCotacao.getInstance().cotacaoSelecionada;
    }

    override public function fault(data:Object):void {
        GerenciadorCotacao.getInstance().resetTimerAnalise();
        super.fault(data)
    }
    override public function result(data:Object):void {
        super.resetCommunication();
        if (hasErrors(data)) return;
        var event:ResultEvent = data as ResultEvent;
        var itensAnalise:ArrayCollection = event.result as ArrayCollection;
        if(itensAnalise && itensAnalise.length>0 ){
            cotacaoSelecionada.analise.setItensAsSaved(itensAnalise);
            cotacaoSelecionada.analise.alterada = false;
            cotacaoSelecionada.codigoEstadoCotacao = EstadoCotacao.EM_ANALISE;
        }else{
            fault(data);
        }
        GerenciadorCotacao.getInstance().resetTimerAnalise();
        //Session.getInstance().messageHandler.showTextMessage("Análise", "Análise salva com sucesso", Icons.SUCESSFUL_24);
    }

}
}
