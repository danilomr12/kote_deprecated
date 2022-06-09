package br.com.cotecom.control.commands.pedido {
import br.com.cotecom.control.delegates.RespostaDelegate;
import br.com.cotecom.control.events.LoadEvent;
import br.com.cotecom.control.responders.resposta.LoadPedidoResponder;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class LoadPedidoCommand implements ICommand{

    public function LoadPedidoCommand() {
    }

    public function execute( event : CairngormEvent ):void{
        var loadEvent:LoadEvent = event as LoadEvent;

        var responder:LoadPedidoResponder = new LoadPedidoResponder ();
        var delegate:RespostaDelegate = new RespostaDelegate();

        delegate.loadPedidoById(responder, loadEvent.id);
    }
}
}
