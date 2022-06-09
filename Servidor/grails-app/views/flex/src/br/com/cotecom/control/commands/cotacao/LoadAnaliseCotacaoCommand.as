package br.com.cotecom.control.commands.cotacao {
import br.com.cotecom.control.delegates.CotacaoDelegate;
import br.com.cotecom.control.events.LoadEvent;
import br.com.cotecom.control.responders.cotacao.PopulateAnaliseCotacaoResponder;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class LoadAnaliseCotacaoCommand implements ICommand {

    public function LoadAnaliseCotacaoCommand() {
    }

    public function execute(event:CairngormEvent):void {
        var loadEvent:LoadEvent = event as LoadEvent;
        var responder:PopulateAnaliseCotacaoResponder = new PopulateAnaliseCotacaoResponder();
        var delegate:CotacaoDelegate = new CotacaoDelegate();
        delegate.loadAnaliseCotacao(responder, loadEvent.id);
    }
}
}
