package br.com.cotecom.control.commands.pedido {
import br.com.cotecom.control.delegates.RespostaDelegate;
import br.com.cotecom.control.events.pedido.FaturePedidoEvent;
import br.com.cotecom.control.responders.resposta.FaturePedidoResponder;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class FaturePedidoCommand implements ICommand{

    public function FaturePedidoCommand() {
    }

    public function execute( event : CairngormEvent ):void{
        var faturePedidoEvent:FaturePedidoEvent = event as FaturePedidoEvent;

        var responder:FaturePedidoResponder = new FaturePedidoResponder();
        var delegate:RespostaDelegate = new RespostaDelegate();

        delegate.faturePedido(responder, faturePedidoEvent.pedidoId);
    }
}
}
