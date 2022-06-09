package br.com.cotecom.control.commands.aplicacao
{
import br.com.cotecom.model.Session;
import br.com.cotecom.view.screens.BaseCaixaDeEntrada;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class ShowViewCaixaDeEntradaCommand implements ICommand {
		
		[Bindable]
		private var session:Session = Session.getInstance();
			
		public function ShowViewCaixaDeEntradaCommand(){
		}
		
		public function execute(event:CairngormEvent): void {
			session.viewState.applicationViewStack.selectedIndex = 1;
			var baseCaixaEtd:BaseCaixaDeEntrada = session.viewState.applicationViewStack.selectedChild as BaseCaixaDeEntrada;
			baseCaixaEtd.selectedIndex = 0;
			session.caixaDeEntradaService.getAllItens();
		}
	}
}