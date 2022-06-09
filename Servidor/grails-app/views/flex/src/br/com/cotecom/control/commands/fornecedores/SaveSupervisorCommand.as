package br.com.cotecom.control.commands.fornecedores{

import br.com.cotecom.control.delegates.UsuarioDelegate;
import br.com.cotecom.control.events.SaveEvent;
import br.com.cotecom.control.responders.fornecedores.SaveSupervisorResponder;
import br.com.cotecom.model.domain.dtos.usuarios.Usuario;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class SaveSupervisorCommand implements ICommand{
		
		public function SaveSupervisorCommand(){}
		
		public function execute( event : CairngormEvent ) : void{
			var saveEvent:SaveEvent = event as SaveEvent;
			
			var responder:SaveSupervisorResponder = new SaveSupervisorResponder();
			var delegate:UsuarioDelegate = new UsuarioDelegate();
			delegate.saveSupervisor(responder, saveEvent.objectToSave as Usuario);
		}
	}
}
