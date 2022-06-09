package br.com.cotecom.control.events.cotacao
{
import com.adobe.cairngorm.control.CairngormEvent;

import flash.events.Event;

public class AtualizeRespostasEvent extends CairngormEvent
	{
		
		public static const EVENT_NAME:String = "br.com.cotecom.control.events.cotacao.AtualizeRespostasEvent";
		public var qtddRespostasConcluidas:int;
		public var idCotacao:*;
		
		public function AtualizeRespostasEvent(qtddRespostasConcluidas:int, idCotacao:* , bubbles:Boolean=false, cancelable:Boolean=false){
			this.qtddRespostasConcluidas = qtddRespostasConcluidas;
			this.idCotacao = idCotacao;
			super(EVENT_NAME, bubbles, cancelable);
		}
		
		public override function clone():Event {
			return new AtualizeRespostasEvent(this.qtddRespostasConcluidas, this.idCotacao, this.bubbles, this.cancelable);
		}
		
	}
}