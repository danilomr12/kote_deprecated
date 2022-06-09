package br.com.cotecom.control.responders.usuario {

import br.com.cotecom.control.responders.aplicacao.ServerResponder;
import br.com.cotecom.model.Session;
import br.com.cotecom.model.domain.dtos.usuarios.Empresa;
import br.com.cotecom.view.util.Icons;

import mx.rpc.events.ResultEvent;

public class SaveEmpresaClienteResponder extends ServerResponder{

    public var model:Session = Session.getInstance();

    public function SaveEmpresaClienteResponder() {
        super();
    }

    override public function result(data:Object):void {
        super.resetCommunication();
        if (hasErrors(data)) return;

        var item:ResultEvent = data as ResultEvent;
        if(item.result is Empresa){
            Session.getInstance().user.empresa = item.result as Empresa;
            Session.getInstance().messageHandler.showTextMessage("Empresa", "Empresa salva com sucesso", Icons.SAVE_24)
        }else{
            fault(data);
        }
    }
}
}
