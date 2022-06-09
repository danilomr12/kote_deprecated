package br.com.cotecom.control.events.aplicacao
{
import com.adobe.cairngorm.control.CairngormEvent;

import flash.events.Event;

public class ShowViewNovaCotacaoEvent extends CairngormEvent {
		
		public static const EVENT_NAME:String = "ShowViewNovaCotacao";
		
		public function ShowViewNovaCotacaoEvent(){
			super(EVENT_NAME);
		}
		
		public override function clone() : Event {
			return new ShowViewNovaCotacaoEvent();
		}
		
	}

}