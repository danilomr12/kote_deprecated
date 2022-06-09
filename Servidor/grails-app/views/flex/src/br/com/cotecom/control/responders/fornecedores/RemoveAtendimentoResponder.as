package br.com.cotecom.control.responders.fornecedores {
import br.com.cotecom.control.responders.aplicacao.ServerResponder;
import br.com.cotecom.model.Session;
import br.com.cotecom.model.domain.dtos.usuarios.Usuario;
import br.com.cotecom.model.services.comprador.GerenciadorRepresentantes;
import br.com.cotecom.view.util.Icons;

import mx.rpc.events.ResultEvent;

public class RemoveAtendimentoResponder extends ServerResponder{

    public var session:Session = Session.getInstance();
    public var gerenciadorFornecedores:GerenciadorRepresentantes = GerenciadorRepresentantes.getInstance();
    public var representante:Usuario;

    public function RemoveAtendimentoResponder(representante:Usuario) {
        super();
        this.representante = representante;
    }

    override public function result(data:Object):void {
        super.resetCommunication();
        if (hasErrors(data)) return;

        var item:ResultEvent = data as ResultEvent;
        if (item.result is Boolean && item.result) {
            session.messageHandler.showTextMessage("Fornecedores", "Atendimento removido com sucesso", Icons.REVERT_24);
            var representante:Boolean = item.result as Boolean;
            gerenciadorFornecedores.removaRepresentante(this.representante);
        } else {
            fault(item);
        }
    }

}
}