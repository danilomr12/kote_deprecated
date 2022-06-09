package br.com.cotecom.control.commands.usuario {
import br.com.cotecom.control.delegates.UsuarioDelegate;
import br.com.cotecom.control.events.LoadEvent;
import br.com.cotecom.control.responders.usuario.LoadUsuarioLogadoResponder;
import br.com.cotecom.model.Session;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class LoadUsuarioLogadoCommand implements ICommand {

    public var model:Session = Session.getInstance();

    public function LoadUsuarioLogadoCommand() {
    }

    public function execute(event:CairngormEvent):void {
        var loadEvent:LoadEvent = event as LoadEvent;

        var responder:LoadUsuarioLogadoResponder = new LoadUsuarioLogadoResponder(model.user);
        var delegate:UsuarioDelegate = new UsuarioDelegate();
        delegate.loadUsuarioLogado(responder);
    }
}
}
		
			
		