package br.com.cotecom.control.events{
import com.adobe.cairngorm.control.CairngormEvent;

import flash.events.Event;

public class SaveEvent extends CairngormEvent{
		
		public static const PRODUTO : String = "SaveProduto";
		public static const REPRESENTANTE : String = "SaveRepresentante";
		public static const COTACAO : String = "SaveCotacao";
		public static const ITEM_COTACAO : String = "SaveItemCotacao";
		public static const RESPOSTA : String = "SaveResposta";
		public static const ANALISE_COTACAO : String = "SaveAnaliseCotacao";
		public static const SUPERVISOR : String = "SaveSupervisor";
		public static const EMPRESA_FORNECEDOR : String = "SaveEmpresaFornecedor";
        public static const EMPRESA_CLIENTE:String = "SaveEmpresaCliente";
        public static const COMPRADOR : String = "SaveComprador";
		
		public var objectToSave:Object;

		
		public function SaveEvent( type:String, objectToSave:Object ){
			super( type );
			this.objectToSave = objectToSave;
		}

		public override function clone() : Event{
			return new SaveEvent( this.type, this.objectToSave );
		}
	}
}
