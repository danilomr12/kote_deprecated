package br.com.cotecom.control.events{

import com.adobe.cairngorm.control.CairngormEvent;

import flash.events.Event;

public class AcceptEvent extends CairngormEvent{
		
		public static const RESPOSTA : String = "AcceptResposta";
		
		public var objectToAccept:Object;
		
		public function AcceptEvent( type:String, objectToAccept:Object ){
			super(type);
			this.objectToAccept = objectToAccept;
		}
		
		public override function clone() : Event{
			return new AcceptEvent( this.type, this.objectToAccept );
		}
	}
}