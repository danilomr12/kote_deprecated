package br.com.cotecom.control.responders.catalogo {
import br.com.cotecom.control.responders.aplicacao.ServerResponder;
import br.com.cotecom.model.Session;
import br.com.cotecom.model.domain.dtos.Produto;
import br.com.cotecom.model.services.comprador.GerenciadorCatalogo;
import br.com.cotecom.view.util.Icons;

import mx.collections.ArrayCollection;
import mx.rpc.events.ResultEvent;

public class SaveProdutoResponder extends ServerResponder{

    public var gerenciador:GerenciadorCatalogo = GerenciadorCatalogo.getInstance();
    public var model:Session = Session.getInstance();
    public var targetProduto:Produto;

    public function SaveProdutoResponder(target:Produto) {
        this.targetProduto = target;
        super();
    }

    override public function result(event:Object):void {
        super.resetCommunication();
        if (hasErrors(event)) return;

        var item:ResultEvent = event as ResultEvent;
        var itens:ArrayCollection = new ArrayCollection();

        if (item.result is ArrayCollection) {
            itens = item.result as ArrayCollection
        } else {
            var prod:Produto = item.result as Produto;
            if(!gerenciador.substituaProduto(prod)){
                if(ehNovoProduto()){
                    gerenciador.produtosEncontrados.addItem(prod);
                }else{
                    gerenciador.substituaProdutoPorNovoProduto(targetProduto, prod);
                }
            }
        }
        gerenciador.produtosEncontrados.refresh();

        model.messageHandler.showTextMessage("Catálogo", "Produto(s) salvo(s) com sucesso", Icons.SUCESSFUL_24)
    }

    private function ehNovoProduto():Boolean {
        return targetProduto.id == null;
    }

    private function getEqualsIdItem(arr:ArrayCollection, prod:Produto):Produto {
        for each(var item:Produto in arr) {
            if (item.equalsId(prod)) {
                return item;
            }
        }
        return null;
    }
}
}
