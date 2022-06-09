package br.com.cotecom.control.events.cotacao
{
import com.adobe.cairngorm.control.CairngormEvent;

import flash.events.Event;

public class EnviaCotacaoParaFornecedorEvent extends CairngormEvent {
		
		public static const EVENT_NAME:String = "EnviaCotacaoParaFornecedor";
		public var idCotacao:*;
		public var idRepresentante:*;

		public function EnviaCotacaoParaFornecedorEvent(idCotacao:*, idRepresentante:*) {
			super(EVENT_NAME);
			this.idCotacao = idCotacao;
			this.idRepresentante = idRepresentante;
		}
		
		public override function clone():Event{
			return new EnviaCotacaoParaFornecedorEvent(this.idCotacao, this.idRepresentante);
		}
	}
}