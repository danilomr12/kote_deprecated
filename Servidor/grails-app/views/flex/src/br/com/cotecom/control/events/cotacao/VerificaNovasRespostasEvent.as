package br.com.cotecom.control.events.cotacao {
import com.adobe.cairngorm.control.CairngormEvent;

import flash.events.Event;

public class VerificaNovasRespostasEvent extends CairngormEvent {

    public static const EVENT_NAME:String = "VerificaNovasRespostas";
    public var cotacaoId:int;

    public function VerificaNovasRespostasEvent(cotacaId:int = 0) {
        super(EVENT_NAME);
        this.cotacaoId = cotacaId;
    }

    public override function clone():Event{
        return new VerificaNovasRespostasEvent(this.cotacaoId);
    }
}
}
