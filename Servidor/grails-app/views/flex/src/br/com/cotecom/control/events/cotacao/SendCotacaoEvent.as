package br.com.cotecom.control.events.cotacao
{
import br.com.cotecom.model.domain.tela.TelaCotacaoDTO;

import com.adobe.cairngorm.control.CairngormEvent;

import flash.events.Event;

public class SendCotacaoEvent extends CairngormEvent {
		
		public static const NAME : String = "SendCotacao";
		public var criacaoCotacaoDTO:TelaCotacaoDTO;

		public function SendCotacaoEvent(criacaoCotacaoDTO:TelaCotacaoDTO) {
			super(NAME);
			this.criacaoCotacaoDTO = criacaoCotacaoDTO;
		}

		public override function clone():Event {
			return new SendCotacaoEvent(this.criacaoCotacaoDTO);
		}


	}
}