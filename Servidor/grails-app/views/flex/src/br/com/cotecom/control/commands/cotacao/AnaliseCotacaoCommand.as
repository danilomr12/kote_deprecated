package br.com.cotecom.control.commands.cotacao {
import br.com.cotecom.control.delegates.CotacaoDelegate;
import br.com.cotecom.control.events.cotacao.CotacaoEvent;
import br.com.cotecom.control.responders.cotacao.PopulateAnaliseCotacaoResponder;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class AnaliseCotacaoCommand implements ICommand {

    public function AnaliseCotacaoCommand() {
    }

    public function execute(event:CairngormEvent):void {
        var cotacaoEvent:CotacaoEvent = event as CotacaoEvent;
        var responder:PopulateAnaliseCotacaoResponder = new PopulateAnaliseCotacaoResponder();
        var delegate:CotacaoDelegate = new CotacaoDelegate();
        delegate.analiseCotacao(responder, cotacaoEvent.id);
    }
}
}
