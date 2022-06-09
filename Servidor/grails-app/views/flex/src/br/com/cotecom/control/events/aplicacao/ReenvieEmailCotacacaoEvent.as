package br.com.cotecom.control.events.aplicacao {
import com.adobe.cairngorm.control.CairngormEvent;

import flash.events.Event;

public class ReenvieEmailCotacacaoEvent extends CairngormEvent {

    public static const EVENT_NAME:String = "ReenvieEmailCotacacao";
    public var respostaId:int;

    public function ReenvieEmailCotacacaoEvent(respostaId:int) {
        super(EVENT_NAME);
        this.respostaId = respostaId

    }

    public override function clone() : Event {
        return new ReenvieEmailCotacacaoEvent(this.respostaId);
    }

}
}