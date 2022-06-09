package br.com.cotecom.control.responders.cotacao {
import br.com.cotecom.control.events.aplicacao.ShowViewCaixaDeEntradaEvent;
import br.com.cotecom.control.responders.aplicacao.ServerResponder;
import br.com.cotecom.model.Session;
import br.com.cotecom.model.services.comprador.GerenciadorCotacao;
import br.com.cotecom.model.utils.EstadoCotacao;
import br.com.cotecom.view.util.Icons;
import mx.rpc.events.ResultEvent;

public class LoadPedidosResponder extends ServerResponder {
    private var gerenciadorCotacao:GerenciadorCotacao = GerenciadorCotacao.getInstance();

    public function LoadPedidosResponder() {
        super();
    }

    override public function result(data:Object):void {
        super.resetCommunication();
        if (hasErrors(data)) return;
        var evento:ResultEvent = data as ResultEvent;
        if (data == null) {
            fault(data);
        }
        //gerenciadorCotacao.cotacaoSelecionada.pedidos = evento.result as ArrayCollection;
        gerenciadorCotacao.cotacaoSelecionada.codigoEstadoCotacao = EstadoCotacao.AGUARDANDO_PEDIDOS;
        gerenciadorCotacao.cotacaoSelecionada.analise.cotacao.codigoEstadoCotacao = EstadoCotacao.AGUARDANDO_PEDIDOS;
        /*var analise:AnaliseCotacaoDTO = gerenciadorCotacao.cotacaoSelecionada.analise;
         analise.cotacao.pedidos = evento.result as ArrayCollection;
         analise.cotacao.codigoEstadoCotacao = EstadoCotacao.AGUARDANDO_PEDIDOS;
         gerenciadorCotacao.cotacaoSelecionada.analise = new AnaliseCotacaoDTO();
         gerenciadorCotacao.cotacaoSelecionada.analise = analise;*/
        new ShowViewCaixaDeEntradaEvent().dispatch();
        var model:Session = Session.getInstance();
        model.messageHandler.showTextMessage("Envio", "Pedidos enviados com sucesso!", Icons.SUCESSFUL_24);
    }

}
}