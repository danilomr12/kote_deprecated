package br.com.cotecom.control.events.aplicacao
{
import com.adobe.cairngorm.control.CairngormEvent;

import flash.events.Event;

public class ShowViewCotacaoEnviadaEvent extends CairngormEvent {
		
		public static const EVENT_NAME:String = "ShowViewCotacaoEnviada";
		public var cotacaoId:int;
        public var codigoEstadoCotacao:int;
		
		public function ShowViewCotacaoEnviadaEvent(cotacaoId:int, codigoEstadoCotacao:int) {
			super(EVENT_NAME);
			this.cotacaoId = cotacaoId;
            this.codigoEstadoCotacao = codigoEstadoCotacao;
		}
		public override function clone() : Event {
			return new ShowViewCotacaoEnviadaEvent(this.cotacaoId, this.codigoEstadoCotacao);
		}

	}
}