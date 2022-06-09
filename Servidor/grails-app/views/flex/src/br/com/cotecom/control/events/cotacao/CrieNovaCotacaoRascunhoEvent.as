package br.com.cotecom.control.events.cotacao {
import com.adobe.cairngorm.control.CairngormEvent;

import flash.events.Event;

public class CrieNovaCotacaoRascunhoEvent extends CairngormEvent{

    public static const EVENT_NAME:String = "CrieNovaCotacaoRascunho";

    public function CrieNovaCotacaoRascunhoEvent() {
        super(EVENT_NAME);
    }

    public override function clone():Event{
			return new CrieNovaCotacaoRascunhoEvent();
    }
}
}
