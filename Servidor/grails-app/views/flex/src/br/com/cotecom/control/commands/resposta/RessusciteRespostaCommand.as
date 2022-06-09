package br.com.cotecom.control.commands.resposta {
import br.com.cotecom.control.delegates.RespostaDelegate;
import br.com.cotecom.control.events.resposta.RespostaEvent;
import br.com.cotecom.control.responders.resposta.RessusciteRespostaResponder;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class RessusciteRespostaCommand implements ICommand{

		public function RessusciteRespostaCommand(){}

		public function execute( event : CairngormEvent ) : void{
			var respostaEvent:RespostaEvent = event as RespostaEvent;

			var responder:RessusciteRespostaResponder = new RessusciteRespostaResponder ();
			var delegate:RespostaDelegate = new RespostaDelegate();

			delegate.ressusciteResposta(responder, respostaEvent.object.id);
		}
	}
}
