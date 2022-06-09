package br.com.cotecom.control.events{
import com.adobe.cairngorm.control.CairngormEvent;

import flash.events.Event;

public class SendSaveEvent extends CairngormEvent{
		
		public static const ITEM_RESPOSTA : String = "SendSaveItemResposta";
		
		public var objectToSend:Object;

		public function SendSaveEvent( type:String, objectToSend:Object ){
			super(type);
			this.objectToSend = objectToSend;
		}

		public override function clone() : Event{
			return new SendSaveEvent( this.type, this.objectToSend );
		}
	}
}
