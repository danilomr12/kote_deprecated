package br.com.cotecom.control.events.cotacao {
import br.com.cotecom.model.domain.dtos.Produto;

import com.adobe.cairngorm.control.CairngormEvent;

import flash.events.Event;

import mx.collections.ArrayCollection;

public class AdicioneProdutoACotacaoEvent extends CairngormEvent {
    public static const EVENT_NAME:String = "AdicioneProdutoACotacao";
    public var produto:Produto;
    public var quantidade:int;
    public var alvo:ArrayCollection;

    public function AdicioneProdutoACotacaoEvent(produto:Produto, alvo:ArrayCollection, quantidade:int) {
        super(EVENT_NAME);
        this.alvo = alvo;
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public override function clone():Event {
        return new AdicioneProdutoACotacaoEvent(this.produto, this.alvo, this.quantidade);
    }

}
}		
