package br.com.cotecom.control.commands.usuario {
import br.com.cotecom.control.delegates.UsuarioDelegate;
import br.com.cotecom.control.events.SaveEvent;
import br.com.cotecom.control.responders.usuario.SaveCompradorResponder;
import br.com.cotecom.model.domain.dtos.usuarios.Usuario;
import br.com.cotecom.model.services.comprador.GerenciadorCotacao;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class SaveCompradorCommand implements ICommand {

    [Bindable]
    public var gerenciadorCotacao:GerenciadorCotacao = GerenciadorCotacao.getInstance();

    public function SaveCompradorCommand() {
    }

    public function execute(event:CairngormEvent):void {
        var evt:SaveEvent = event as SaveEvent;
        var responder:SaveCompradorResponder = new SaveCompradorResponder();
        var delegate:UsuarioDelegate = new UsuarioDelegate();
        delegate.updateCadastroComprador(responder, evt.objectToSave as Usuario);
    }
}

}
