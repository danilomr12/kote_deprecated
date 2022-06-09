package br.com.cotecom.control.commands.resposta {
import br.com.cotecom.control.delegates.RespostaDelegate;
import br.com.cotecom.control.events.resposta.RespostaEvent;
import br.com.cotecom.control.responders.resposta.CanceleRespostaResponder;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class CanceleRespostaCommand implements ICommand{

		public function CanceleRespostaCommand(){}

		public function execute( event : CairngormEvent ) : void{
			var respostaEvent:RespostaEvent = event as RespostaEvent;

			var responder:CanceleRespostaResponder = new CanceleRespostaResponder();
			var delegate:RespostaDelegate = new RespostaDelegate();

			delegate.canceleResposta(responder, respostaEvent.object.id);
		}
	}
}

