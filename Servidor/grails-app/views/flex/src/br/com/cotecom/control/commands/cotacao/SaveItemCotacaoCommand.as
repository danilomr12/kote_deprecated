package br.com.cotecom.control.commands.cotacao {
import br.com.cotecom.control.delegates.CotacaoDelegate;
import br.com.cotecom.control.events.SaveEvent;
import br.com.cotecom.control.responders.cotacao.SaveItemCotacaoResponder;
import br.com.cotecom.model.domain.cotacao.ItemCotacaoDTO;
import br.com.cotecom.model.services.comprador.GerenciadorCotacao;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class SaveItemCotacaoCommand implements ICommand {

    [Bindable]
    public var gerenciadorCotacao:GerenciadorCotacao = GerenciadorCotacao.getInstance();

    public function SaveItemCotacaoCommand() {
    }

    public function execute(event:CairngormEvent):void {
        var saveEvent:SaveEvent = event as SaveEvent;
        var responder:SaveItemCotacaoResponder = new SaveItemCotacaoResponder();
        var delegate:CotacaoDelegate = new CotacaoDelegate();
        delegate.saveItemCotacao(responder, saveEvent.objectToSave as ItemCotacaoDTO)
    }
}

}
