package br.com.cotecom.control.events
{
import com.adobe.cairngorm.control.CairngormEvent;

import flash.events.Event;
import flash.utils.ByteArray;

public class ImporteEvent extends CairngormEvent{
		
		public var tipo:String;
		public var byteArray:ByteArray;
        public var params:Object;
		public static const PLANILHA_PRODUTOS:String = "importePlanilhaProdutos";
        public static const PLANILHA_RESPOSTA:String = "importePlanilhaResposta";
		
		public function ImporteEvent(tipo:String, arquivo:ByteArray){
			super(tipo);
			this.tipo = tipo;
			this.byteArray = arquivo;
            params = new Object();
		}
	
		public override function clone():Event{
			return new ImporteEvent(this.tipo, this.byteArray);
		}
		
	}
}