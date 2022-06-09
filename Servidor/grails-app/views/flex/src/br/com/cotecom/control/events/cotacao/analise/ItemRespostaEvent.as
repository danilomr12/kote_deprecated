package br.com.cotecom.control.events.cotacao.analise {

import br.com.cotecom.model.domain.dtos.ItemAnaliseRespostaDTO;

import com.adobe.cairngorm.control.CairngormEvent;

import flash.events.Event;

public class ItemRespostaEvent extends CairngormEvent {
		
		public static const MELHOR_RESPOSTA_ALTERADA : String = "ItemRespostaMelhorRespostaAlterada";
		
		public var itemResposta:ItemAnaliseRespostaDTO;
		
		public function ItemRespostaEvent (type:String, itemResposta:ItemAnaliseRespostaDTO = null, bubbles:Boolean = false) {
			super(type, bubbles);
			this.itemResposta = itemResposta
		}
		
		public override function clone() : Event {
			return new ItemRespostaEvent (this.type , this.itemResposta);
		}
	}
}
