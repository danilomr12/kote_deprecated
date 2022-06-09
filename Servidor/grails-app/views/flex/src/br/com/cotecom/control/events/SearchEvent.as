package br.com.cotecom.control.events{

import com.adobe.cairngorm.control.CairngormEvent;

import flash.events.Event;

import mx.collections.ArrayCollection;

public class SearchEvent extends CairngormEvent{
		
		public static const PRODUTO : String = "SearchProduto";
		public static const REPRESENTANTE_SEM_ATENDIMENTO : String = "SearchRepresentante";
		public static const COTACAO : String = "SearchCotacao";
		
		public var query:String;
		public var alvo:ArrayCollection;
		
		public function SearchEvent( type:String, query:String, alvo:ArrayCollection) {
			super( type );
			this.alvo = alvo;
			this.query = query;
		}
		
		public override function clone() : Event {
			return new SearchEvent( this.type, this.query, this.alvo);
		}
	}
}
