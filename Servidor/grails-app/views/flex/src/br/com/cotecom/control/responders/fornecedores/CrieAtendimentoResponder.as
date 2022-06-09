package br.com.cotecom.control.responders.fornecedores {
import br.com.cotecom.control.responders.aplicacao.ServerResponder;
import br.com.cotecom.model.Session;
import br.com.cotecom.model.domain.dtos.usuarios.Usuario;
import br.com.cotecom.model.services.comprador.GerenciadorRepresentantes;
import br.com.cotecom.view.util.Icons;

import mx.rpc.events.ResultEvent;

public class CrieAtendimentoResponder extends ServerResponder{

    public var session:Session = Session.getInstance();
    public var gerenciadorFornecedores:GerenciadorRepresentantes = GerenciadorRepresentantes.getInstance();

    public function CrieAtendimentoResponder() {
        super();
    }

    override public function result(data:Object):void {
        super.resetCommunication();
        if (hasErrors(data)) return;

        var item:ResultEvent = data as ResultEvent;
        if (item.result is Usuario) {
            session.messageHandler.showTextMessage("Fornecedores", "Atendimento adicionado com sucesso", Icons.SAVE_24);
            var representante:Usuario = item.result as Usuario;
            gerenciadorFornecedores.adicioneOuSubstituaRepresentante(representante);
        } else {
            fault(item);
        }
    }

}
}