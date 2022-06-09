package br.com.cotecom.control.commands.catalogo {
import br.com.cotecom.control.delegates.ProdutoDelegate;
import br.com.cotecom.control.events.RemoveEvent;
import br.com.cotecom.control.responders.catalogo.RemoveProdutoResponder;
import br.com.cotecom.model.Session;
import br.com.cotecom.model.domain.dtos.Produto;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class RemoveProdutoCommand implements ICommand {

    [Bindable]
    public var model:Session = Session.getInstance();

    public function RemoveProdutoCommand() {
    }

    public function execute(event:CairngormEvent):void {
        var removeProdutoEvent:RemoveEvent = event as RemoveEvent;

        var responder:RemoveProdutoResponder = new RemoveProdutoResponder();
        var delegate:ProdutoDelegate = new ProdutoDelegate();
        delegate.removeProduto(responder, removeProdutoEvent.objectToRemove as Produto);
    }
}
}
