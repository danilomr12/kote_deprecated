package br.com.cotecom.control.responders.catalogo {
import br.com.cotecom.control.responders.aplicacao.ServerResponder;
import br.com.cotecom.model.Session;
import br.com.cotecom.model.services.comprador.GerenciadorCatalogo;

import mx.rpc.events.ResultEvent;

public class RemoveProdutoResponder extends ServerResponder{

    public var gerenciadorCatalogo:GerenciadorCatalogo = GerenciadorCatalogo.getInstance();
    public var model:Session = Session.getInstance();

    public function RemoveProdutoResponder() {
        super();
    }

    override public function result(event:Object):void {
        super.resetCommunication();
        if (hasErrors(event)) return;
        var item:ResultEvent = event as ResultEvent;
        if (item.result is Boolean) {
            var removed:Boolean = item.result as Boolean;
            if (removed) {
                model.messageHandler.showTextMessage("Catálogo", "Produto(s) deletado(s) com sucesso");
            } else {
                model.messageHandler.showTextMessage("Catálogo", "Não foi possível deletar os produto(s) selecionados! Tente novamente mais tarde.");

            }
        }
    }

}
}
