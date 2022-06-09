package br.com.cotecom.control.commands.fornecedores {
import br.com.cotecom.control.delegates.UsuarioDelegate;
import br.com.cotecom.control.events.RemoveEvent;
import br.com.cotecom.control.events.fornecedores.CrieAtendimentoEvent;
import br.com.cotecom.control.responders.fornecedores.CrieAtendimentoResponder;
import br.com.cotecom.control.responders.fornecedores.RemoveAtendimentoResponder;
import br.com.cotecom.model.domain.dtos.usuarios.Usuario;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class RemoveAtendimentoCommand implements ICommand {

    public function RemoveAtendimentoCommand() {
    }

    public function execute(event:CairngormEvent):void {
        var removeAtendimentoEvent:RemoveEvent = event as RemoveEvent;

        var responder:RemoveAtendimentoResponder = new RemoveAtendimentoResponder(removeAtendimentoEvent.objectToRemove as Usuario);
        var delegate:UsuarioDelegate = new UsuarioDelegate();
        delegate.removeAtedimento(responder, removeAtendimentoEvent.objectToRemove as Usuario);
    }
}
}
