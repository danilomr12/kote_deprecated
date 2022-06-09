package br.com.cotecom.control.commands.fornecedores{

import br.com.cotecom.control.delegates.UsuarioDelegate;
import br.com.cotecom.control.events.LoadEvent;
import br.com.cotecom.control.responders.fornecedores.LoadRepresentanteResponder;
import br.com.cotecom.model.services.comprador.GerenciadorRepresentantes;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class LoadRepresentanteCommand implements ICommand{
		
		public function LoadRepresentanteCommand(){}
 	
		public function execute( event : CairngormEvent ):void{
			var loadEvent:LoadEvent = event as LoadEvent;
			
			var responder:LoadRepresentanteResponder = 
					new LoadRepresentanteResponder(GerenciadorRepresentantes.getInstance().representantesComAtendimento);
			var delegate:UsuarioDelegate = new UsuarioDelegate();
			delegate.loadRepresentantes(responder);
		}
	}
}
