package br.com.cotecom.control.commands.cotacao {
import br.com.cotecom.control.delegates.CotacaoDelegate;
import br.com.cotecom.control.events.cotacao.CrieNovaCotacaoRascunhoEvent;
import br.com.cotecom.control.responders.cotacao.CrieNovaCotacaoRascunhoResponder;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class CrieNovaCotacaoRascunhoConmmand implements ICommand{

    public function CrieNovaCotacaoRascunhoConmmand() {}

    public function execute(event:CairngormEvent):void {
        var crieNovaCotacaoEvent:CrieNovaCotacaoRascunhoEvent = event as CrieNovaCotacaoRascunhoEvent;
        var responder:CrieNovaCotacaoRascunhoResponder =
                new CrieNovaCotacaoRascunhoResponder();
        var delegate:CotacaoDelegate = new CotacaoDelegate();
        delegate.crieNovaCotacaoRascunho(responder);
    }
}
}
