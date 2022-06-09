package br.com.cotecom.control.commands.cotacao {
import br.com.cotecom.control.delegates.CotacaoDelegate;
import br.com.cotecom.control.events.DownloadFileEvent;
import br.com.cotecom.control.responders.cotacao.DownloadPlanilhaAnaliseCotacaoResponder;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class DownloadPlanilhaAnaliseCotacaoCommand implements ICommand{

    public function DownloadPlanilhaAnaliseCotacaoCommand() {}

    public function execute(event:CairngormEvent):void {
        var downloadEvent:DownloadFileEvent= event as DownloadFileEvent;
        var responder:DownloadPlanilhaAnaliseCotacaoResponder =
                new DownloadPlanilhaAnaliseCotacaoResponder();
        var delegate:CotacaoDelegate = new CotacaoDelegate();
        delegate.downloadPlanilhaAnaliseCotacao(responder, downloadEvent.params);
    }
}
}