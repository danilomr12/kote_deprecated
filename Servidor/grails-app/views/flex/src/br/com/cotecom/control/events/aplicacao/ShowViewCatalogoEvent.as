package br.com.cotecom.control.events.aplicacao
{
import com.adobe.cairngorm.control.CairngormEvent;

import flash.events.Event;

public class ShowViewCatalogoEvent extends CairngormEvent{
		
		public static const EVENT_NAME:String = "ShowViewCatalogo";
	
		public function ShowViewCatalogoEvent() {
			super(EVENT_NAME);
		}
	
		public override function clone():Event {
			return new ShowViewCatalogoEvent();
		}
	
	}
}