package br.com.cotecom.control.events.cotacao {
import com.adobe.cairngorm.control.CairngormEvent;

import flash.events.Event;

public class CotacaoEvent extends CairngormEvent {

    public static const ANALISE_COTACAO:String = "AnaliseCotacaoEvent";
    public static const FINALIZE_COTACAO:String = "FinalizeCotacaoEvent";

    public var id:*;

    public function CotacaoEvent(type:String, id:* = null) {
        super(type);
        this.id = id;
    }

    public override function clone():Event {
        return new CotacaoEvent(this.type, this.id);
    }
}
}
