package br.com.cotecom.control.commands.fornecedores
{
import br.com.cotecom.control.delegates.UsuarioDelegate;
import br.com.cotecom.control.responders.fornecedores.LoadSupervisoresResponder;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class LoadSupervisorCommand implements ICommand
	{
		public function LoadSupervisorCommand(){}
		
		public function execute(event:CairngormEvent):void
		{
			var responder:LoadSupervisoresResponder = 
				new LoadSupervisoresResponder();
			var delegate:UsuarioDelegate = new UsuarioDelegate();
			delegate.loadSupervisores(responder);
		}
	}
}