package br.com.cotecom.control.commands.cotacao {
import br.com.cotecom.control.delegates.CotacaoDelegate;
import br.com.cotecom.control.events.cotacao.CotacaoEvent;
import br.com.cotecom.control.responders.cotacao.FinalizeCotacaoResponder;
import br.com.cotecom.model.Session;
import br.com.cotecom.model.services.comprador.GerenciadorCotacao;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class FinalizeCotacaoCommand implements ICommand {

    public function FinalizeCotacaoCommand() {
    }

    public var model:Session = Session.getInstance();
    private var gerenciadorCotacao:GerenciadorCotacao = GerenciadorCotacao.getInstance();

    public function execute(event:CairngormEvent):void {
        var finalizeCotacaoEvent:CotacaoEvent = event as CotacaoEvent;

        var responder:FinalizeCotacaoResponder = new FinalizeCotacaoResponder(gerenciadorCotacao.cotacaoSelecionada);
        var delegate:CotacaoDelegate = new CotacaoDelegate();

        delegate.finalizeCotacao(responder, finalizeCotacaoEvent.id)
    }
}

}
