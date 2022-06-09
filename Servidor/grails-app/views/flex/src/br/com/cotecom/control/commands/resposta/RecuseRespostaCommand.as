package br.com.cotecom.control.commands.resposta{

import br.com.cotecom.control.delegates.RespostaDelegate;
import br.com.cotecom.control.events.resposta.RespostaEvent;
import br.com.cotecom.control.responders.resposta.RecuseRespostaResponder;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class RecuseRespostaCommand implements ICommand{
		
		public function RecuseRespostaCommand(){}
 		
		public function execute( event : CairngormEvent ) : void{
			var refuseEvent:RespostaEvent = event as RespostaEvent;
			
			var responder:RecuseRespostaResponder = new RecuseRespostaResponder();
			var delegate:RespostaDelegate = new RespostaDelegate();
						
			delegate.recuseResposta(responder, refuseEvent.object as Number);
		}
	}
}
