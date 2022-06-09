package br.com.cotecom.control.responders.fornecedores{
import br.com.cotecom.control.responders.aplicacao.ServerResponder;
import br.com.cotecom.model.Session;
import br.com.cotecom.model.domain.dtos.usuarios.Empresa;
import br.com.cotecom.model.services.resposta.GerenciadorFornecedores;
import br.com.cotecom.view.util.Icons;

import mx.rpc.events.ResultEvent;

public class SaveEmpresaFornecedorResponder extends ServerResponder{

    public var gerenciador:GerenciadorFornecedores = GerenciadorFornecedores.getInstance();
    public var model:Session = Session.getInstance();

    public function SaveEmpresaFornecedorResponder() {
        super();
    }

    override public function result(data:Object):void {
        super.resetCommunication();
        if (hasErrors(data)) return;
        var item:ResultEvent = data as ResultEvent;
        if(item.result is Empresa){
            gerenciador.adicioneOuSubstituaFornecedor(item.result as Empresa);
            Session.getInstance().messageHandler.showTextMessage("Fornecedores", "Empresa salva com sucesso", Icons.SAVE_24)
        }else{
            fault(data);
        }
    }
}
}
