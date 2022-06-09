package br.com.cotecom.control.events.aplicacao
{
import com.adobe.cairngorm.control.CairngormEvent;

import flash.events.Event;

public class ShowViewPedidoEvent extends CairngormEvent {
		
		public static const EVENT_NAME:String = "ShowViewPedido";
		public var pedidoId:int
		
		public function ShowViewPedidoEvent(pedidoId:int) {
			super(EVENT_NAME);
			this.pedidoId = pedidoId
		}
		public override function clone() : Event {
			return new ShowViewPedidoEvent(this.pedidoId);
		}
	}
}