package br.com.cotecom.control.events.aplicacao
{
import com.adobe.cairngorm.control.CairngormEvent;

import flash.events.Event;

public class ShowViewRespostaEvent extends CairngormEvent {
		
		public static const EVENT_NAME:String = "ShowViewResposta";
		public var respostaId:int;
		
		public function ShowViewRespostaEvent(respostaId:int) {
			super(EVENT_NAME);
			this.respostaId = respostaId
		}
		public override function clone() : Event {
			return new ShowViewRespostaEvent(this.respostaId);
		}
	}
}