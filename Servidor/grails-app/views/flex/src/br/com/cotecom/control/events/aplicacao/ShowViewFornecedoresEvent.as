package br.com.cotecom.control.events.aplicacao
{
import com.adobe.cairngorm.control.CairngormEvent;

import flash.events.Event;

public class ShowViewFornecedoresEvent extends CairngormEvent {
		
		public static const EVENT_NAME:String = "ShowViewFornecedores";
		
		public function ShowViewFornecedoresEvent() {
			super(EVENT_NAME);
		}
		
		public override function clone():Event{
			return new ShowViewFornecedoresEvent();
		}

	}
}