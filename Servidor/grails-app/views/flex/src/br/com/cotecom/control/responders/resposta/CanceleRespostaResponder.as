package br.com.cotecom.control.responders.resposta {
import br.com.cotecom.control.responders.aplicacao.ServerResponder;
import br.com.cotecom.model.Session;
import br.com.cotecom.model.domain.resposta.Resposta;
import br.com.cotecom.model.domain.resposta.TelaResposta;
import br.com.cotecom.model.services.resposta.GerenciadorResposta;
import br.com.cotecom.model.utils.EstadoResposta;
import br.com.cotecom.view.util.Icons;

import mx.rpc.events.ResultEvent;

public class CanceleRespostaResponder extends ServerResponder{

    public function CanceleRespostaResponder() {
    }

    override public function result(event:Object):void {
        super.resetCommunication();
        if (hasErrors(event)) return;

        var evento:ResultEvent = event as ResultEvent;
        if (evento == null || !evento.result || !(evento.result is Boolean)) {
            fault(event);
        } else {
            if(evento.result){
                var telaResposta:TelaResposta = GerenciadorResposta.getInstance().telaResposta;
                telaResposta.resposta.status = EstadoResposta.CANCELADA;
                GerenciadorResposta.getInstance().telaResposta = null;
                GerenciadorResposta.getInstance().telaResposta = telaResposta;
                Session.getInstance().messageHandler.showTextMessage("Resposta", "Resposta cancelada", Icons.INFORMATION);
            }
        }
    }
}
}
