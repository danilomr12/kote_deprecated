package br.com.cotecom.control.commands.resposta{

import br.com.cotecom.control.delegates.RespostaDelegate;
import br.com.cotecom.control.events.LoadEvent;
import br.com.cotecom.control.responders.resposta.LoadRespostaResponder;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class LoadRespostaCommand implements ICommand{
				
		public function LoadRespostaCommand(){}
 
		public function execute( event : CairngormEvent ):void{
			var loadEvent:LoadEvent = event as LoadEvent;
			
			var responder:LoadRespostaResponder = new LoadRespostaResponder();
			var delegate:RespostaDelegate = new RespostaDelegate();
			
			delegate.loadRespostaById(responder, loadEvent.id);
		}
	}
}
