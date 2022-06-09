package br.com.cotecom.control.commands.catalogo {
import br.com.cotecom.control.delegates.ProdutoDelegate;
import br.com.cotecom.control.events.SearchEvent;
import br.com.cotecom.control.responders.catalogo.SearchProdutosResponder;
import br.com.cotecom.model.services.comprador.GerenciadorCatalogo;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class SearchProdutoCommand implements ICommand {

    public var model:GerenciadorCatalogo = GerenciadorCatalogo.getInstance();

    public function SearchProdutoCommand() {
    }

    public function execute(event:CairngormEvent):void {
        var searchProdutosEvent:SearchEvent = event as SearchEvent;

        var responder:SearchProdutosResponder =
                new SearchProdutosResponder(searchProdutosEvent.alvo);
        var delegate:ProdutoDelegate = new ProdutoDelegate();

        delegate.searchProduto(responder, searchProdutosEvent.query);
    }
}
}
