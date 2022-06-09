package br.com.cotecom.control.commands.resposta {
import br.com.cotecom.control.delegates.RespostaDelegate;
import br.com.cotecom.control.events.DownloadFileEvent;
import br.com.cotecom.control.responders.resposta.DownloadPlanilhaRespostaResponder;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class DownloadPlanilhaRespostaCommand implements ICommand {

    public function DownloadPlanilhaRespostaCommand() {
    }

    public function execute(event:CairngormEvent):void {
        var downloadEvent:DownloadFileEvent= event as DownloadFileEvent;
        var responder:DownloadPlanilhaRespostaResponder =
                new DownloadPlanilhaRespostaResponder ();
        var delegate:RespostaDelegate = new RespostaDelegate();
        delegate.downloadPlanilhaResposta(responder, downloadEvent.params);
    }
}
}