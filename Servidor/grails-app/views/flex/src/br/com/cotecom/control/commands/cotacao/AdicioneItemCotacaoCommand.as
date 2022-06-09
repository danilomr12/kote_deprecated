package br.com.cotecom.control.commands.cotacao {
import br.com.cotecom.control.delegates.CotacaoDelegate;
import br.com.cotecom.control.events.cotacao.AdicioneItemCotacaoEvent;
import br.com.cotecom.control.responders.cotacao.AdicioneItemCotacaoResponder;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class AdicioneItemCotacaoCommand implements ICommand{

    public function AdicioneItemCotacaoCommand() {}

    public function execute(event:CairngormEvent):void {
        var addEvent:AdicioneItemCotacaoEvent = event as AdicioneItemCotacaoEvent;
        var responder:AdicioneItemCotacaoResponder = new AdicioneItemCotacaoResponder();
        var delegate:CotacaoDelegate = new CotacaoDelegate();

        delegate.adicioneItensCotacao(responder, addEvent.itensCotacao);
    }
}
}
