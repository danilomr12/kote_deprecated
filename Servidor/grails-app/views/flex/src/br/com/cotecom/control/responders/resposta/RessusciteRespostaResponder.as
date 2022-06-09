package br.com.cotecom.control.responders.resposta {
import br.com.cotecom.control.responders.aplicacao.ServerResponder;
import br.com.cotecom.model.Session;
import br.com.cotecom.model.domain.resposta.Resposta;
import br.com.cotecom.model.domain.resposta.TelaResposta;
import br.com.cotecom.model.services.resposta.GerenciadorResposta;
import br.com.cotecom.model.utils.EstadoResposta;
import br.com.cotecom.view.util.Icons;

import mx.rpc.events.ResultEvent;

public class RessusciteRespostaResponder extends ServerResponder{

    public function RessusciteRespostaResponder()  {}

    override public function result(event:Object):void {
        super.resetCommunication();
        if (hasErrors(event)) return;

        var evento:ResultEvent = event as ResultEvent;
        if (evento == null || !evento.result || !(evento.result is Resposta)) {
            fault(event);
        } else {
            var telaResposta:TelaResposta = GerenciadorResposta.getInstance().telaResposta;
            telaResposta.resposta = evento.result as Resposta;
            GerenciadorResposta.getInstance().telaResposta = telaResposta;
             Session.getInstance().messageHandler.showTextMessage("Resposta", "Resposta reativada", Icons.INFORMATION);
        }
    }
}
}
