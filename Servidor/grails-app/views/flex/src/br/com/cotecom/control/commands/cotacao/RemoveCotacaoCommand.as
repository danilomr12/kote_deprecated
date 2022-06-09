package br.com.cotecom.control.commands.cotacao {
import br.com.cotecom.control.delegates.CotacaoDelegate;
import br.com.cotecom.control.events.RemoveEvent;
import br.com.cotecom.control.responders.cotacao.CanceleCotacaoResponder;
import br.com.cotecom.model.Session;
import br.com.cotecom.model.services.comprador.GerenciadorCotacao;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class RemoveCotacaoCommand implements ICommand {
    public var model:Session = Session.getInstance();
    private var gerenciadorCotacao:GerenciadorCotacao = GerenciadorCotacao.getInstance();

    public function RemoveCotacaoCommand() {
    }

    public function execute(event:CairngormEvent):void {
        var removeCotacaoEvent:RemoveEvent = event as RemoveEvent;

        var responder:CanceleCotacaoResponder = new CanceleCotacaoResponder(gerenciadorCotacao.telaCotacaoDTO.cotacaoDTO);
        var delegate:CotacaoDelegate = new CotacaoDelegate();

        delegate.canceleCotacao(responder, removeCotacaoEvent.objectToRemove)
    }
}
}
