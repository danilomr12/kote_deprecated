package br.com.cotecom.control.events.aplicacao
{
import com.adobe.cairngorm.control.CairngormEvent;

import flash.events.Event;

public class ShowViewCotacaoProcessandoEnvioDeRespostasEvent extends CairngormEvent {
		
		public static const EVENT_NAME:String = "ShowViewCotacaoProcessandoEnvioDeRespostas";
		
		public function ShowViewCotacaoProcessandoEnvioDeRespostasEvent() {
			super(EVENT_NAME);
		}
		public override function clone() : Event {
			return new ShowViewCotacaoProcessandoEnvioDeRespostasEvent();
		}
	}
}