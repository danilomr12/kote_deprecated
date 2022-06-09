package br.com.cotecom.control.commands.cotacao {
import br.com.cotecom.control.delegates.CotacaoDelegate;
import br.com.cotecom.control.events.aplicacao.ReenvieEmailCotacacaoEvent;
import br.com.cotecom.control.responders.cotacao.ReenvieEmailCotacaoResponder;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class ReenvieEmailCotacacaoCommand implements ICommand {

    public function ReenvieEmailCotacacaoCommand() {
    }

    public function execute(event:CairngormEvent):void {
        var envieEmailCotacaoEvent:ReenvieEmailCotacacaoEvent = event as ReenvieEmailCotacacaoEvent;
        var delegate:CotacaoDelegate = new CotacaoDelegate();
        var responder:ReenvieEmailCotacaoResponder = new ReenvieEmailCotacaoResponder();
        delegate.reenvieEmailCotacao(responder, envieEmailCotacaoEvent.respostaId);

    }
}
}