package br.com.cotecom.model.services.comprador
{
import br.com.cotecom.control.events.ImporteEvent;
import br.com.cotecom.control.events.RemoveEvent;
import br.com.cotecom.control.events.SaveEvent;
import br.com.cotecom.control.events.SearchEvent;
import br.com.cotecom.model.domain.dtos.Produto;

import flash.utils.ByteArray;

import mx.collections.ArrayCollection;

public class GerenciadorCatalogo
{
    [Bindable]
    public var produtosEncontrados:ArrayCollection;

    public function salveProduto(produto:Produto):void{
        var saveEvent:SaveEvent = new SaveEvent(SaveEvent.PRODUTO, produto);
        saveEvent.dispatch();
    }

    public function searchProduto(query:String = "", alvo:ArrayCollection = null):void{
        var searchEvent:SearchEvent= new SearchEvent(SearchEvent.PRODUTO, query, alvo);
        searchEvent.dispatch();
    }

    public function deleteProduto(produto:Produto):void{
        var deleteEvent:RemoveEvent= new RemoveEvent(RemoveEvent.PRODUTO,produto);
        deleteEvent.dispatch();
    }

    public function importePlanilha(data:ByteArray):void{
        var importeEvent:ImporteEvent = new ImporteEvent(ImporteEvent.PLANILHA_PRODUTOS, data);
        importeEvent.dispatch();
    }

    private static var instance:GerenciadorCatalogo;

    public function GerenciadorCatalogo(enforcer:SingletonEnforcer){
        if(!enforcer is SingletonEnforcer)
            throw new Error("use the get instance function");
        this.produtosEncontrados = new ArrayCollection();
    }

    public static function getInstance():GerenciadorCatalogo{
        if(instance == null)
            instance = new GerenciadorCatalogo(new SingletonEnforcer());
        return instance;
    }

    public function substituaProduto(produto:Produto):Boolean {
        for each(var prod:Produto in this.produtosEncontrados){
            if(prod.id == produto.id){
                var index:int = this.produtosEncontrados.getItemIndex(prod);
                if(index){
                    this.produtosEncontrados.setItemAt(produto,index);
                    return true;
                }
            }
        }
        return false;
    }

    public function substituaProdutoPorNovoProduto(target:Produto, prod:Produto):void {
        var itenIndex:int = this.produtosEncontrados.getItemIndex(buscaProdutoPorId(target.id));
        if(itenIndex>=0){
            this.produtosEncontrados.removeItemAt(itenIndex);
            this.produtosEncontrados.addItemAt(prod, itenIndex)
        }
    }

    public function buscaProdutoPorId(id:int):Produto {
        for each(var prod:Produto in this.produtosEncontrados){
            if(prod.id == id){
                return prod;
            }
        }
        return null;
    }
}
}
class SingletonEnforcer{};