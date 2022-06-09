package br.com.cotecom.control.responders.fornecedores
{
import br.com.cotecom.control.responders.aplicacao.ServerResponder;
import br.com.cotecom.model.services.resposta.GerenciadorFornecedores;

import mx.collections.ArrayCollection;
import mx.rpc.events.ResultEvent;

public class LoadEmpresasFornecedoresResponder extends ServerResponder {

    public var target:ArrayCollection;
    public var gerenciadorFornecedores:GerenciadorFornecedores = GerenciadorFornecedores.getInstance();

    public function LoadEmpresasFornecedoresResponder(){
        super();
    }

    override public function result(data:Object):void {
        super.resetCommunication();
        if (hasErrors(data)) return;
        var evento:ResultEvent = data as ResultEvent;
        var fornecedores:ArrayCollection = evento.result as ArrayCollection;
        if(fornecedores == null){
            fault(data)
        }else{
            this.gerenciadorFornecedores.empresas.removeAll();
            this.gerenciadorFornecedores.empresas.addAll(fornecedores)
        }
    }
}
}