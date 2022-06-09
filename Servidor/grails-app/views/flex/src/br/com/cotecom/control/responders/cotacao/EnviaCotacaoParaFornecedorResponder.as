package br.com.cotecom.control.responders.cotacao {
import br.com.cotecom.control.responders.aplicacao.ServerResponder;
import br.com.cotecom.model.Session;
import br.com.cotecom.model.domain.dtos.usuarios.Usuario;
import br.com.cotecom.model.services.comprador.GerenciadorCotacao;
import br.com.cotecom.view.util.Icons;
import mx.rpc.events.ResultEvent;

public class EnviaCotacaoParaFornecedorResponder extends ServerResponder{
    public var representante:Usuario;

    public function EnviaCotacaoParaFornecedorResponder() {
        super();
    }

    override public function result(event:Object):void {
        super.resetCommunication();
        if (hasErrors(event)) return;

        var evento:ResultEvent = event as ResultEvent;
        if(evento.result is Boolean && evento.result){
            var model:Session = Session.getInstance();
            GerenciadorCotacao.getInstance().cotacaoSelecionada.alterada = true;
            model.messageHandler.showTextMessage("Contação enviada", "Processando envio ao representante, após enviada " +
                    "aparecerá na caixa de entrada junto à cotação",Icons.SEND_24);
        }
    }
}
}
