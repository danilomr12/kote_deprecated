package br.com.cotecom.control.events.resposta{

import com.adobe.cairngorm.control.CairngormEvent;

import flash.events.Event;

public class RespostaEvent extends CairngormEvent{
		
		public static const RECUSAR : String = "Recusar";
        public static const RESSUSCITAR: String = "Ressuscitar";
        public static const CANCELAR: String = "Cancelar";

		public var object:Object;

		public function RespostaEvent( type:String, objectToRefuse:Object ){
			super( type );
			this.object = objectToRefuse;
		}

		public override function clone() : Event{
			return new RespostaEvent( this.type, this.object );
		}
	}
}
