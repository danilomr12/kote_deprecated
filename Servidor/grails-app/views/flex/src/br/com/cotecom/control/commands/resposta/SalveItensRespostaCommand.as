package br.com.cotecom.control.commands.resposta{

import br.com.cotecom.control.delegates.RespostaDelegate;
import br.com.cotecom.control.events.SaveEvent;
import br.com.cotecom.control.responders.resposta.SaveItensRespostaResponder;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

import mx.collections.ArrayCollection;

public class SalveItensRespostaCommand implements ICommand{
		
		public function SalveItensRespostaCommand(){}
 		
		public function execute( event : CairngormEvent ) : void{
			var saveEvent:SaveEvent = event as SaveEvent;
			
			var responder:SaveItensRespostaResponder = new SaveItensRespostaResponder(saveEvent.objectToSave as ArrayCollection);
			var delegate:RespostaDelegate = new RespostaDelegate();
			
			delegate.saveResposta(responder, saveEvent.objectToSave);
		}
	}
}
