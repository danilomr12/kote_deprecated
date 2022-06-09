package br.com.cotecom.control.commands.cotacao
{
import br.com.cotecom.control.delegates.CotacaoDelegate;
import br.com.cotecom.control.events.cotacao.GeraPedidosEvent;
import br.com.cotecom.control.responders.cotacao.EnviePedidosResponder;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class GeraEEnviaPedidosCommand implements ICommand{

    public function GeraEEnviaPedidosCommand(){}

    public function execute(event: CairngormEvent):void{
        var geraEvent:GeraPedidosEvent = event as GeraPedidosEvent;
        var responder:EnviePedidosResponder = new EnviePedidosResponder();
        var delegate:CotacaoDelegate = new CotacaoDelegate();
        delegate.gereEEnviePedidos(responder,geraEvent.analise);
    }
}
}