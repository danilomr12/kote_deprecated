package br.com.cotecom.control.events.aplicacao
{
import com.adobe.cairngorm.control.CairngormEvent;

import flash.events.Event;

public class ShowViewCotacaoRascunhoEvent extends CairngormEvent {
		
		public static const EVENT_NAME:String = "ShowViewCotacaoRascunho";
		public var cotacaoId:int;
		
		public function ShowViewCotacaoRascunhoEvent(cotacaoId:int) {
			super(EVENT_NAME);
			this.cotacaoId = cotacaoId
		}
		public override function clone() : Event {
			return new ShowViewCotacaoRascunhoEvent(this.cotacaoId);
		}
	}
}