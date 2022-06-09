package br.com.cotecom.control.events.cotacao{

import com.adobe.cairngorm.control.CairngormEvent;

import flash.events.Event;

public class ShowViewEvent extends CairngormEvent{
		
		public static const EVENT_NAME:String = "ShowView";
		
		public static const NOVA_COTACAO:int = 0;
		public static const CAIXA_DE_ENTRADA:int = 1;
		public static const CATALOGO:int = 2;
		public static const FORNECEDORES:int = 3;
		public static const COTACAO:int = 4;
		public static const RESPOSTA:int = 5;
		public static const PEDIDO:int = 6;
		
		public var viewIndex:uint;
		
		public function ShowViewEvent(viewIndex:int) {
			super(EVENT_NAME);
			this.viewIndex = viewIndex;
		}
		
		public override function clone() : Event {
			return new ShowViewEvent(this.viewIndex);
		}
	}
}
