package br.com.cotecom.control.responders.catalogo {
import br.com.cotecom.control.responders.aplicacao.ServerResponder;
import br.com.cotecom.model.Session;

import mx.rpc.events.ResultEvent;

public class ImportePlanilhaProdutosResponder extends ServerResponder {

    public var model:Session = Session.getInstance();

    public function ImportePlanilhaProdutosResponder() {
        super();
    }

    override public function result(event:Object):void {
        super.resetCommunication();
        if (hasErrors(event)) return;

        var objeto:ResultEvent = event as ResultEvent;
        var importou:Boolean = objeto.result;
        if (importou) {
            model.messageHandler.showTextMessage("Importação", "Produtos importados com sucesso");
        } else {

        }
    }

}
}