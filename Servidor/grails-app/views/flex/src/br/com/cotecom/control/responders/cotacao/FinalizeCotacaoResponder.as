package br.com.cotecom.control.responders.cotacao {
import br.com.cotecom.control.responders.aplicacao.ServerResponder;
import br.com.cotecom.model.Session;
import br.com.cotecom.model.domain.cotacao.CotacaoDTO;
import br.com.cotecom.model.domain.dtos.AnaliseCotacaoDTO;
import br.com.cotecom.model.services.comprador.GerenciadorCotacao;
import br.com.cotecom.model.utils.EstadoCotacao;
import br.com.cotecom.view.util.Icons;

import mx.rpc.events.ResultEvent;

public class FinalizeCotacaoResponder extends ServerResponder {

    public var model:Session = Session.getInstance();
    public var gerenciadorCotacao:GerenciadorCotacao = GerenciadorCotacao.getInstance();
    public var target:CotacaoDTO;

    public function FinalizeCotacaoResponder(target:CotacaoDTO) {
        super();
        this.target = target;
    }

    override public function result(data:Object):void {
        super.resetCommunication();
        if (hasErrors(data)) return;

        var evento:ResultEvent = data as ResultEvent;
        if (evento == null || !evento.result)
            fault(data);
        var analise:AnaliseCotacaoDTO = gerenciadorCotacao.cotacaoSelecionada.analise;
        analise.cotacao.codigoEstadoCotacao = EstadoCotacao.FINALIZADA;
        gerenciadorCotacao.cotacaoSelecionada = analise.cotacao;
        gerenciadorCotacao.cotacaoSelecionada.analise = new AnaliseCotacaoDTO();
        gerenciadorCotacao.cotacaoSelecionada.analise = analise;
        model.messageHandler.showTextMessage("Cotacao", "Cotação finalizada", Icons.INFORMATION);
    }

}
}
