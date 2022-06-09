package br.com.cotecom.control.commands.cotacao {
import br.com.cotecom.control.delegates.CotacaoDelegate;
import br.com.cotecom.control.events.cotacao.SendCotacaoEvent;
import br.com.cotecom.control.responders.cotacao.SendCotacaoResponder;
import br.com.cotecom.model.services.comprador.GerenciadorCotacao;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class SendCotacaoCommand implements ICommand {

    public var gerenciadorCotacao:GerenciadorCotacao = GerenciadorCotacao.getInstance();

    public function SendCotacaoCommand() {
    }

    public function execute(event:CairngormEvent):void {
        var sendCotacaoEvent:SendCotacaoEvent = event as SendCotacaoEvent;

        var responder:SendCotacaoResponder = new SendCotacaoResponder(gerenciadorCotacao.telaCotacaoDTO);
        var delegate:CotacaoDelegate = new CotacaoDelegate();

        delegate.sendCotacao(responder, sendCotacaoEvent.criacaoCotacaoDTO)
    }
}
}