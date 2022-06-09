package br.com.cotecom.control.responders.cotacao {
import br.com.cotecom.control.responders.aplicacao.ServerResponder;
import br.com.cotecom.model.domain.dtos.AnaliseCotacaoDTO;
import br.com.cotecom.model.domain.dtos.AnaliseRespostaDTO;
import br.com.cotecom.model.services.comprador.GerenciadorCotacao;

import mx.collections.ArrayCollection;
import mx.rpc.events.ResultEvent;

public class PopulateAnaliseCotacaoResponder extends ServerResponder {

    public var gerenciadorCotacao:GerenciadorCotacao = GerenciadorCotacao.getInstance();
    public var analise:AnaliseCotacaoDTO;

    public function PopulateAnaliseCotacaoResponder() {
        super();
    }

    override public function result(data:Object):void {
        super.resetCommunication();
        if (hasErrors(data)) return;

        var event:ResultEvent = data as ResultEvent;
        if (event.result != null) {
            this.analise = event.result as AnaliseCotacaoDTO;
            initRespostas(this.analise.respostas);
            gerenciadorCotacao.cotacaoSelecionada = this.analise.cotacao;
            gerenciadorCotacao.cotacaoSelecionada.analise = analise;
        }
    }

    private function initRespostas(respostas:ArrayCollection):void {
        for each(var resposta:AnaliseRespostaDTO in respostas) {
            resposta.analise = analise;
        }
    }

}
}
