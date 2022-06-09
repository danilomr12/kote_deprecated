package br.com.cotecom.control.commands.fornecedores{

import br.com.cotecom.control.delegates.UsuarioDelegate;
import br.com.cotecom.control.events.SaveEvent;
import br.com.cotecom.control.responders.fornecedores.SaveRepresentanteResponder;
import br.com.cotecom.model.domain.dtos.usuarios.Usuario;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class SaveRepresentanteCommand implements ICommand{

    public function SaveRepresentanteCommand(){}

    public function execute( event : CairngormEvent ) : void{
        var saveRepresentanteEvent:SaveEvent = event as SaveEvent;

        var responder:SaveRepresentanteResponder = new SaveRepresentanteResponder();
        var delegate:UsuarioDelegate = new UsuarioDelegate();
        delegate.saveRepresentante(responder, saveRepresentanteEvent.objectToSave as Usuario);

    }
}
}
