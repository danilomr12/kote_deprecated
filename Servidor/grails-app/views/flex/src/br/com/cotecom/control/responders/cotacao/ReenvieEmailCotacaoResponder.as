package br.com.cotecom.control.responders.cotacao {
import br.com.cotecom.control.responders.aplicacao.ServerResponder;
import br.com.cotecom.model.Session;
import br.com.cotecom.view.util.Icons;

import mx.rpc.events.ResultEvent;

public class ReenvieEmailCotacaoResponder extends ServerResponder{

    private var model:Session = Session.getInstance();

    public function ReenvieEmailCotacaoResponder() {
        super();
    }

    override public function result(data:Object):void {
        super.resetCommunication();
        if (hasErrors(data)) return;

        var evento:ResultEvent = data as ResultEvent;
        if (evento == null) {
            fault(data);
        } else {
            model.messageHandler.showTextMessage("Cotacao", "E-mail de cotação enviado", Icons.SUCESSFUL_24);
        }
    }

}
}