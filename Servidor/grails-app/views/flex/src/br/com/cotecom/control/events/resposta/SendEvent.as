package br.com.cotecom.control.events.resposta{
import com.adobe.cairngorm.control.CairngormEvent;

import flash.events.Event;

public class SendEvent extends CairngormEvent{
		
		public static const RESPOSTA : String = "SendResposta";
		
		public var objectToSend:Object;

		public function SendEvent( type:String, objectToSend:Object ){
			super(type);
			this.objectToSend = objectToSend;
		}

		public override function clone() : Event{
			return new SendEvent( this.type, this.objectToSend );
		}
	}
}
