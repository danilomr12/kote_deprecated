package br.com.cotecom.control.events.cotacao {
import com.adobe.cairngorm.control.CairngormEvent;

import flash.events.Event;

import mx.collections.ArrayCollection;

public class AdicioneItemCotacaoEvent extends CairngormEvent{

    public static const EVENT_NAME:String = "AdicioneItemCotacao";
    public var itensCotacao:ArrayCollection;

    public function AdicioneItemCotacaoEvent(itensCotacao:ArrayCollection) {
        super( EVENT_NAME );
        this.itensCotacao = itensCotacao;
    }

    public override function clone() : Event {
			return new AdicioneItemCotacaoEvent(this.itensCotacao);
    }
}
}
