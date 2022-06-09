package br.com.cotecom.control.responders.usuario {
import br.com.cotecom.control.responders.aplicacao.ServerResponder;
import br.com.cotecom.model.Session;
import br.com.cotecom.model.domain.dtos.usuarios.Usuario;
import br.com.cotecom.view.util.Icons;

import mx.containers.TitleWindow;
import mx.managers.PopUpManager;
import mx.rpc.events.ResultEvent;

public class LoginResponder extends ServerResponder {
    private var session:Session = Session.getInstance();
    public var popup:Object;

    public function LoginResponder(popup:Object) {
        super();
        this.popup = popup;
    }

    public override function result(data:Object):void {
        if (hasErrors(data)) return;
        super.resetCommunication();
        var result:ResultEvent = data as ResultEvent;
        if (result.result is Usuario) {
            session.user = result.result as Usuario;
            PopUpManager.removePopUp(popup as TitleWindow);
            session.communicationHandler.resetCommunication();
            session.messageHandler.showTextMessage("Bem-vindo", "Você logou com sucesso!", Icons.INFORMATION);
        }
    }
}
}