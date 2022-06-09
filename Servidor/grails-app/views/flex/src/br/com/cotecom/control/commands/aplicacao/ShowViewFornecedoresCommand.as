package br.com.cotecom.control.commands.aplicacao
{
import br.com.cotecom.model.Session;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class ShowViewFornecedoresCommand implements ICommand {
		
		private var session:Session = Session.getInstance();
		
		public function ShowViewFornecedoresCommand() {}
		
		public function execute(event:CairngormEvent):void {
			session.viewState.applicationViewStack.selectedIndex = 3;
		}
	}
}