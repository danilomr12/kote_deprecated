package br.com.cotecom.control.commands.aplicacao
{
import br.com.cotecom.model.Session;
import br.com.cotecom.view.screens.BaseCaixaDeEntrada;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class ShowViewPedidoCommand implements ICommand {
		
		private var session:Session = Session.getInstance();
		
		public function ShowViewPedidoCommand() {}
		
		public function execute(event:CairngormEvent):void {
			session.viewState.applicationViewStack.selectedIndex = 1;
			var bCxEntrada:BaseCaixaDeEntrada = session.viewState.applicationViewStack.selectedChild as BaseCaixaDeEntrada;
			bCxEntrada.selectedIndex = 4; 
		}
	}
}