package br.com.cotecom.control.responders.fornecedores{

import br.com.cotecom.control.responders.aplicacao.ServerResponder;
import br.com.cotecom.model.Session;
import br.com.cotecom.model.domain.dtos.usuarios.TipoUsuario;
import br.com.cotecom.model.domain.dtos.usuarios.Usuario;
import br.com.cotecom.model.services.resposta.GerenciadorFornecedores;
import br.com.cotecom.view.util.Icons;
import mx.rpc.events.ResultEvent;

public class SaveSupervisorResponder extends ServerResponder{

    public var gerenciador:GerenciadorFornecedores = GerenciadorFornecedores.getInstance();
    private var model:Session = Session.getInstance();

    public function SaveSupervisorResponder(){
        super();
    }

    override public function result(data:Object):void {
        super.resetCommunication();
        if (hasErrors(data)) return;

        var item:ResultEvent = data as ResultEvent;
        if(item.result is Usuario && item.result.tipo == TipoUsuario.SUPERVISOR){
            gerenciador.adicioneOuSubstituaSupervisor(item.result as Usuario);
            model.messageHandler.showTextMessage("Supervisor", "Supervisor salvo com sucesso", Icons.SUCESSFUL_24);
        }else{
            fault(data);
        }
    }

}
}