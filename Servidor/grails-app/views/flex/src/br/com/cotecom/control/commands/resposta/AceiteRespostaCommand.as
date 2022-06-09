package br.com.cotecom.control.commands.resposta{

import br.com.cotecom.control.delegates.RespostaDelegate;
import br.com.cotecom.control.events.AcceptEvent;
import br.com.cotecom.control.responders.resposta.AceiteRespostaResponder;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class AceiteRespostaCommand implements ICommand{
		
		public function AceiteRespostaCommand(){}
		
		public function execute( event : CairngormEvent ) : void{
			var acceptEvent:AcceptEvent = event as AcceptEvent;
			
			var responder:AceiteRespostaResponder = new AceiteRespostaResponder();
			var delegate:RespostaDelegate = new RespostaDelegate();
			
			delegate.acceptResposta(responder, acceptEvent.objectToAccept as Number);
		}
	}
}
