package br.com.cotecom.control.responders.resposta {
import br.com.cotecom.control.responders.aplicacao.ServerResponder;
import br.com.cotecom.model.Session;
import br.com.cotecom.model.domain.resposta.Resposta;
import br.com.cotecom.model.domain.resposta.TelaResposta;
import br.com.cotecom.model.services.resposta.GerenciadorResposta;
import br.com.cotecom.view.util.Icons;

import mx.rpc.events.ResultEvent;

public class EnvieRespostaResponder extends ServerResponder {

    public var session:Session = Session.getInstance();
    public var gerenciador:GerenciadorResposta = GerenciadorResposta.getInstance();

    public function EnvieRespostaResponder() {
        super();
    }

    public override function result(event:Object):void {
        super.resetCommunication();
        if (hasErrors(event)) return;
        var evento:ResultEvent = event as ResultEvent;
        var resposta:Resposta = evento.result as Resposta;
        var telaResposta:TelaResposta = gerenciador.telaResposta;
        gerenciador.telaResposta = null;
        telaResposta.resposta = resposta;
        gerenciador.telaResposta = telaResposta;
        session.messageHandler.showTextMessage("Cotação", "Resposta de cotação enviada com sucesso. Agora você não pode mais " +
                "editar esta resposta, apenas poderá visualizá-la para informações do estado da cotação.",
                Icons.SUCESSFUL_24);
        gerenciador.editavel = false;
    }

}
}
