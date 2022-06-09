package br.com.cotecom.control.commands.catalogo {
import br.com.cotecom.control.delegates.ProdutoDelegate;
import br.com.cotecom.control.events.DownloadFileEvent;
import br.com.cotecom.control.responders.catalogo.DownloadPlanilhaExemploResponder;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class DownloadPlanilhaExemploCommand implements ICommand {

    public function DownloadPlanilhaExemploCommand() {
    }

    public function execute(event:CairngormEvent):void {
        var downloadEvent:DownloadFileEvent = event as DownloadFileEvent;

        var responder:DownloadPlanilhaExemploResponder = new DownloadPlanilhaExemploResponder();
        var delegate:ProdutoDelegate = new ProdutoDelegate();

        delegate.downloadPlanilhaExemploImportacao(responder);
    }
}
}