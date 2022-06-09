package br.com.cotecom.control.events.pedido{
import com.adobe.cairngorm.control.CairngormEvent;

import flash.events.Event;

public class FaturePedidoEvent extends CairngormEvent{
		
		public static const EVENT_NAME : String = "faturePedido";
		
		public var pedidoId:int;

		public function FaturePedidoEvent( type:String, pedidoId:int ){
			super(type);
			this.pedidoId = pedidoId;
		}

		public override function clone() : Event{
			return new FaturePedidoEvent( this.type, this.pedidoId);
		}
	}
}
