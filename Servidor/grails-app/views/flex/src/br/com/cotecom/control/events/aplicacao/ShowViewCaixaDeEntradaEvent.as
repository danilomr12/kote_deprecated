package br.com.cotecom.control.events.aplicacao
{
import com.adobe.cairngorm.control.CairngormEvent;

import flash.events.Event;

public class ShowViewCaixaDeEntradaEvent extends CairngormEvent {
		
		public static const EVENT_NAME:String = "ShowViewCaixaDeEntrada";
		
		public function ShowViewCaixaDeEntradaEvent() {
			super(EVENT_NAME);
		}
		
		public override function clone() : Event {
			return new ShowViewCaixaDeEntradaEvent();
		}
		
	}
}