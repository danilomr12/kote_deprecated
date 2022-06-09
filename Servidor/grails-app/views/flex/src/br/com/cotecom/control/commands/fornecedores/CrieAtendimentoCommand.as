package br.com.cotecom.control.commands.fornecedores {
import br.com.cotecom.control.delegates.UsuarioDelegate;
import br.com.cotecom.control.events.fornecedores.CrieAtendimentoEvent;
import br.com.cotecom.control.responders.fornecedores.CrieAtendimentoResponder;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class CrieAtendimentoCommand implements ICommand {

    public function CrieAtendimentoCommand() {
    }

    public function execute(event:CairngormEvent):void {
        var crieAtendimentoEvent:CrieAtendimentoEvent = event as CrieAtendimentoEvent;

        var responder:CrieAtendimentoResponder = new CrieAtendimentoResponder();
        var delegate:UsuarioDelegate = new UsuarioDelegate();
        delegate.crieAtedimento(responder, crieAtendimentoEvent.representante);
    }
}
}
