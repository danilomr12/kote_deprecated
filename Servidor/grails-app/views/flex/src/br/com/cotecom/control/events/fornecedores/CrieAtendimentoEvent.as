package br.com.cotecom.control.events.fornecedores
{
import br.com.cotecom.model.domain.dtos.usuarios.Usuario;

import com.adobe.cairngorm.control.CairngormEvent;

import flash.events.Event;

public class CrieAtendimentoEvent extends CairngormEvent{
		
		public static const EVENT_NAME:String = "CrieAtendimento";
		public var representante:Usuario;
		
		public function CrieAtendimentoEvent( type:String, representante:Usuario){
			super( type );
			this.representante = representante;
		}
		
		public override function clone():Event{
			return new CrieAtendimentoEvent(this.type, this.representante );
		}

	}
}