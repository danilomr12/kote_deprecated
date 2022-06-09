package br.com.cotecom.control.commands.pedido {
import br.com.cotecom.control.delegates.RespostaDelegate;
import br.com.cotecom.control.events.DownloadFileEvent;
import br.com.cotecom.control.responders.cotacao.DownloadPlanilhaAnaliseCotacaoResponder;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class DownloadPedidoCommand implements ICommand{

    public function DownloadPedidoCommand() {}

    public function execute(event:CairngormEvent):void {
        var downloadEvent:DownloadFileEvent= event as DownloadFileEvent;
        var responder:DownloadPlanilhaAnaliseCotacaoResponder =
                new DownloadPlanilhaAnaliseCotacaoResponder();
        var delegate:RespostaDelegate = new RespostaDelegate();
        delegate.downloadPedido(responder, downloadEvent.params);
    }
}
}