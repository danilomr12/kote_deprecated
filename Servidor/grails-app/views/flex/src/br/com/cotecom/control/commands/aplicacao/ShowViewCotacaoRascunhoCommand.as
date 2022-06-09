package br.com.cotecom.control.commands.aplicacao
{
import br.com.cotecom.control.events.LoadEvent;
import br.com.cotecom.control.events.aplicacao.ShowViewCotacaoRascunhoEvent;
import br.com.cotecom.model.Session;
import br.com.cotecom.view.screens.BaseCaixaDeEntrada;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class ShowViewCotacaoRascunhoCommand implements ICommand {
		
		private var session:Session = Session.getInstance();
			
		public function ShowViewCotacaoRascunhoCommand() {
		}
		
		public function execute(event:CairngormEvent):void {
			var showViewEvent:ShowViewCotacaoRascunhoEvent = event as ShowViewCotacaoRascunhoEvent;
			session.viewState.applicationViewStack.selectedIndex = 1;
			var baseCxEntrada:BaseCaixaDeEntrada = session.viewState.applicationViewStack.selectedChild as BaseCaixaDeEntrada;
			baseCxEntrada.selectedIndex = 1;
			new LoadEvent(LoadEvent.COTACAO,showViewEvent.cotacaoId).dispatch()
		}
	}
}