package br.com.cotecom.control.events{
import com.adobe.cairngorm.control.CairngormEvent;

import flash.events.Event;

public class StatusMessageChangeEvent extends CairngormEvent{
		
		public static const NAME : String = "statusMessageChange"
		
		public var message:String

		public function StatusMessageChangeEvent(message:String){
			super(NAME)
			this.message = message
		}

		public override function clone() : Event{
			return new StatusMessageChangeEvent( this.message)
		}
	}
}
