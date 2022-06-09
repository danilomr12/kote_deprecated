package br.com.cotecom.control.responders.usuario {
import br.com.cotecom.control.responders.aplicacao.ServerResponder;
import br.com.cotecom.model.Session;
import br.com.cotecom.view.util.Icons;

public class SaveCompradorResponder extends ServerResponder{

    private var model:Session = Session.getInstance();

    public function SaveCompradorResponder() {
        super();
    }

    override public function result(event:Object):void {
        super.resetCommunication();
        if (hasErrors(event)) return;

        if (event == null) {
            fault(event);
        } else {
            model.messageHandler.showTextMessage("Usuario", "Dados salvos com sucesso", Icons.SUCESSFUL_24);
        }
    }
}
}
