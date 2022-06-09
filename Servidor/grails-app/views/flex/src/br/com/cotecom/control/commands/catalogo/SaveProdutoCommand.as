package br.com.cotecom.control.commands.catalogo {
import br.com.cotecom.control.delegates.ProdutoDelegate;
import br.com.cotecom.control.events.SaveEvent;
import br.com.cotecom.control.responders.catalogo.SaveProdutoResponder;
import br.com.cotecom.model.domain.dtos.Produto;
import br.com.cotecom.model.services.comprador.GerenciadorCatalogo;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class SaveProdutoCommand implements ICommand {

    [Bindable]
    public var model:GerenciadorCatalogo = GerenciadorCatalogo.getInstance();

    public function SaveProdutoCommand() {
    }

    public function execute(event:CairngormEvent):void {
        var saveProdutoEvent:SaveEvent = event as SaveEvent;

        var responder:SaveProdutoResponder = new SaveProdutoResponder(saveProdutoEvent.objectToSave as Produto);
        var delegate:ProdutoDelegate = new ProdutoDelegate();
        delegate.saveProduto(responder, saveProdutoEvent.objectToSave);
    }
}
}
