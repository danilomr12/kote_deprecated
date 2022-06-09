package br.com.cotecom.control.commands.catalogo {
import br.com.cotecom.control.delegates.ProdutoDelegate;
import br.com.cotecom.control.events.ImporteEvent;
import br.com.cotecom.control.responders.catalogo.ImportePlanilhaProdutosResponder;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class importePlanilhaProdutosCommand implements ICommand {

    public function importePlanilhaProdutosCommand() {
    }

    public function execute(event:CairngormEvent):void {
        var importEvent:ImporteEvent = event as ImporteEvent;

        var responder:ImportePlanilhaProdutosResponder = new ImportePlanilhaProdutosResponder();
        var delegate:ProdutoDelegate = new ProdutoDelegate();

        delegate.importePlanilhaProdutos(responder, importEvent.byteArray);
    }


}
}