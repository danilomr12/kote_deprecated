package br.com.cotecom.control.responders.cotacao {
import br.com.cotecom.control.responders.aplicacao.ServerResponder;
import br.com.cotecom.model.Session;
import br.com.cotecom.model.domain.cotacao.CotacaoDTO;
import br.com.cotecom.model.domain.cotacao.CotacaoDTO;
import br.com.cotecom.model.domain.dtos.AnaliseCotacaoDTO;
import br.com.cotecom.model.services.comprador.GerenciadorCotacao;
import br.com.cotecom.model.utils.EstadoCotacao;
import br.com.cotecom.view.util.Icons;

import mx.collections.ArrayCollection;

import mx.rpc.events.ResultEvent;

public class EnviePedidosResponder extends ServerResponder{

    public function EnviePedidosResponder() {
        super();
    }

    override public function result(event:Object):void {
        super.resetCommunication();
        if (hasErrors(event)) return;

        var evento:ResultEvent = event as ResultEvent;
        if(evento.result){
            var model:Session = Session.getInstance();
            var gerenciadorCotacao:GerenciadorCotacao = GerenciadorCotacao.getInstance();
            var analise:AnaliseCotacaoDTO = gerenciadorCotacao.cotacaoSelecionada.analise;
            analise.cotacao.codigoEstadoCotacao = EstadoCotacao.AGUARDANDO_PEDIDOS;
            gerenciadorCotacao.cotacaoSelecionada = analise.cotacao;
            gerenciadorCotacao.cotacaoSelecionada.analise = new AnaliseCotacaoDTO();
            gerenciadorCotacao.cotacaoSelecionada.analise = analise;
            model.messageHandler.showTextMessage("Envio", "Pedidos enviados com sucesso!", Icons.SUCESSFUL_24);
        }
    }
}
}