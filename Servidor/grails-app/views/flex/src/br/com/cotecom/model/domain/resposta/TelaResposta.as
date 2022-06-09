package br.com.cotecom.model.domain.resposta{
import br.com.cotecom.model.domain.pedido.Pedido;
import br.com.cotecom.model.domain.cotacao.CotacaoDTO;
import br.com.cotecom.model.domain.dtos.usuarios.Usuario;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="br.com.cotecom.domain.dto.tela.TelaRespostaRepresentanteDTO")]
public class TelaResposta{

    public var itensResposta:ArrayCollection;
    public var resposta:Resposta;
    public var comprador:Usuario;
    public var representante:Usuario;
    public var cotacao:CotacaoDTO;
    public var alterada:Boolean;
    public var pedidos:ArrayCollection;

    public function TelaResposta(
            itensResposta:ArrayCollection = null,
            resposta:* = null,
            comprador:Usuario = null,
            representante:Usuario = null){

        this.itensResposta = itensResposta;
        this.resposta = resposta;
        this.comprador = comprador;
        this.representante = representante;
        this.alterada = false;
    }

    public function setAllItensAsSaved():void {
        for each(var item:ItemResposta in itensResposta){
            item.setSaved()
        }
    }

    public function setItensAsSaved(itens:ArrayCollection):void{
        for each(var item:ItemResposta in itensResposta){
            for each(var itemASerEncontrado:ItemResposta in itens){
                if(item.id == itemASerEncontrado.id)
                    item.setSaved()
            }
        }
    }

    public function getUnsavedItens():ArrayCollection{
        var unsavedItens:ArrayCollection = new ArrayCollection();
        for each(var item:ItemResposta in this.itensResposta){
            if(!item.saved){
                unsavedItens.addItem(item);
            }
        }
        return unsavedItens;
    }

    public function populateItems(itensRespostaFromServer:ArrayCollection):void{
        this.itensResposta = itensRespostaFromServer;
    }

    public function equals(outra:TelaResposta):Boolean{
        if(outra
                &&(this.itensResposta == outra.itensResposta)
                &&(this.resposta == outra.resposta)
                &&(this.comprador == outra.comprador)
                &&(this.representante == outra.representante)){
            return true;
        }
        return false;
    }

    public function get telefones():String{
        var str:String = "";
        for (var i:int = 0; i< this.comprador.telefones.length; i++){
            str += this.comprador.telefones.getItemAt(i);
            if(i<this.comprador.telefones.length -1){
                str += ", ";
            }
        }
        return str;
    }
}
}