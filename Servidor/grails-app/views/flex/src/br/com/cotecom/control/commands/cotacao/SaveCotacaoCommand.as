package br.com.cotecom.control.commands.cotacao {
import br.com.cotecom.control.delegates.CotacaoDelegate;
import br.com.cotecom.control.events.SaveEvent;
import br.com.cotecom.control.responders.cotacao.SaveCotacaoResponder;
import br.com.cotecom.model.domain.tela.TelaCotacaoDTO;
import br.com.cotecom.model.services.comprador.GerenciadorCotacao;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class SaveCotacaoCommand implements ICommand {

    [Bindable]
    public var gerenciadorCotacao:GerenciadorCotacao = GerenciadorCotacao.getInstance();

    public function SaveCotacaoCommand() {
    }

    public function execute(event:CairngormEvent):void {
        var saveEvent:SaveEvent = event as SaveEvent;
        var responder:SaveCotacaoResponder = new SaveCotacaoResponder(saveEvent.objectToSave as TelaCotacaoDTO);
        var delegate:CotacaoDelegate = new CotacaoDelegate();
        delegate.saveCotacao(responder, saveEvent.objectToSave as TelaCotacaoDTO)
    }
}

}
