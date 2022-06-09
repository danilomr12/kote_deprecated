package br.com.cotecom.control.responders.catalogo {
import br.com.cotecom.control.responders.aplicacao.ServerResponder;
import br.com.cotecom.model.Session;
import br.com.cotecom.model.domain.dtos.Produto;
import br.com.cotecom.model.services.comprador.GerenciadorCatalogo;

import mx.collections.ArrayCollection;
import mx.rpc.events.ResultEvent;

public class SearchProdutosResponder extends ServerResponder {

    public var model:Session = Session.getInstance();
    public var gerenciador:GerenciadorCatalogo = GerenciadorCatalogo.getInstance();
    public var alvo:ArrayCollection;

    public function SearchProdutosResponder(alvo:ArrayCollection) {
        super();
        this.alvo = alvo;
    }

    override public function result(event:Object):void {
        super.resetCommunication();
        if (hasErrors(event)) return;
        var evento:ResultEvent = event as ResultEvent;
        var lista:ArrayCollection = evento.result as ArrayCollection;
        if (alvo == null)
            alvo = gerenciador.produtosEncontrados;
        alvo.removeAll();
        for each(var item:Produto in lista) {
            alvo.addItem(item);
        }
    }

}
}
