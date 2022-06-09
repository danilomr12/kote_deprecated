package br.com.cotecom.control.commands.cotacao {
import br.com.cotecom.control.delegates.CotacaoDelegate;
import br.com.cotecom.control.events.cotacao.RemovaItemCotacaoEvent;
import br.com.cotecom.control.responders.cotacao.RemovaItemCotacaoResponder;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class RemovaItemCotacaoCommand implements ICommand{

    public function RemovaItemCotacaoCommand() {}

    public function execute(event:CairngormEvent):void {
        var addEvent:RemovaItemCotacaoEvent= event as RemovaItemCotacaoEvent;
        var responder:RemovaItemCotacaoResponder = new RemovaItemCotacaoResponder();
        var delegate:CotacaoDelegate = new CotacaoDelegate();

        delegate.removaItemCotacao(responder, addEvent.itemCotacao);
    }
}
}
