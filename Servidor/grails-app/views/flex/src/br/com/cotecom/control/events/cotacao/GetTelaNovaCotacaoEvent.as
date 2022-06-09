package br.com.cotecom.control.events.cotacao
{
import com.adobe.cairngorm.control.CairngormEvent;

import flash.events.Event;

public class GetTelaNovaCotacaoEvent extends CairngormEvent {
		
		public static const EVENT_NAME:String = "GetTelaNovaCotacao";
		public var cotacaoId:int	
		
		public function GetTelaNovaCotacaoEvent(cotacaId:int) {
			super(EVENT_NAME);
			this.cotacaoId = cotacaId
		}
		
		public override function clone():Event{
			return new GetTelaNovaCotacaoEvent(this.cotacaoId);
		}
	}
}