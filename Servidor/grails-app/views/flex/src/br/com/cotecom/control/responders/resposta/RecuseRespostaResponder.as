package br.com.cotecom.control.responders.resposta {
import br.com.cotecom.control.responders.aplicacao.ServerResponder;
import br.com.cotecom.model.Session;
import br.com.cotecom.model.domain.resposta.Resposta;
import br.com.cotecom.model.domain.resposta.TelaResposta;
import br.com.cotecom.model.services.resposta.GerenciadorResposta;
import br.com.cotecom.model.utils.EstadoResposta;

import mx.rpc.events.ResultEvent;

public class RecuseRespostaResponder extends ServerResponder {

    public var session:Session = Session.getInstance();
    public var gerenciador:GerenciadorResposta = GerenciadorResposta.getInstance();

    public function RecuseRespostaResponder() {
        super();
    }

    public override function result(event:Object):void {
        super.resetCommunication();
        if (hasErrors(event)) return;
        var evento:ResultEvent = event as ResultEvent;
        var resposta:Resposta = evento.result as Resposta;

        var telaResposta:TelaResposta = GerenciadorResposta.getInstance().telaResposta;
        telaResposta.resposta = resposta;
        GerenciadorResposta.getInstance().telaResposta = null;
        GerenciadorResposta.getInstance().telaResposta = telaResposta;
        gerenciador.editavel = false;
        session.messageHandler.showTextMessage("Cotação Recusada", "cotação recusada com sucesso. Agora você não pode mais editar esta resposta");

    }

}
}
