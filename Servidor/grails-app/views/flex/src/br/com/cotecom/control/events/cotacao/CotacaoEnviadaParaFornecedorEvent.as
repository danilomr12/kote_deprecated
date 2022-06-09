package br.com.cotecom.control.events.cotacao{

import com.adobe.cairngorm.control.CairngormEvent;

import flash.events.Event;

public class CotacaoEnviadaParaFornecedorEvent extends CairngormEvent {
		
		public static const EVENT_NAME : String = "cotacaoEnviadaParaRepresentanteEvent";
				
		public function CotacaoEnviadaParaFornecedorEvent() {
			super(EVENT_NAME);	
		}
		
		public override function clone() : Event {
			return new CotacaoEnviadaParaFornecedorEvent ();
		}
	}
}
