package br.com.cotecom.control.responders.fornecedores
{
import br.com.cotecom.control.responders.aplicacao.ServerResponder;
import br.com.cotecom.model.domain.dtos.usuarios.Usuario;
import br.com.cotecom.model.services.resposta.GerenciadorFornecedores;

import mx.collections.ArrayCollection;
import mx.rpc.events.ResultEvent;

public class LoadSupervisoresResponder extends ServerResponder {

    public var gerenciadorFornecedores:GerenciadorFornecedores = GerenciadorFornecedores.getInstance();

    public function LoadSupervisoresResponder(){
        super();
    }

    override public function result(data:Object):void {
        super.resetCommunication();
        if (hasErrors(data)) return;
        var evento:ResultEvent = data as ResultEvent;
        var supervisores:ArrayCollection = evento.result as ArrayCollection;
        if(supervisores == null){
            fault(data)
        }else{
            for each(var sup:Usuario in supervisores){
                this.gerenciadorFornecedores.adicioneOuSubstituaSupervisor(sup);
            }
        }
    }
}
}