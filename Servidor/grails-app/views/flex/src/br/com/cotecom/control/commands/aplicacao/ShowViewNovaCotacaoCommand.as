package br.com.cotecom.control.commands.aplicacao
{
import br.com.cotecom.model.Session;
import br.com.cotecom.model.services.comprador.GerenciadorCotacao;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class ShowViewNovaCotacaoCommand implements ICommand {
		
		private var session:Session = Session.getInstance()
		
		public function ShowViewNovaCotacaoCommand(){
		}
		
		public function execute(event:CairngormEvent):void {
			GerenciadorCotacao.getInstance().limpeTelaNovaCotacao()
			session.viewState.applicationViewStack.selectedIndex = 0;
		}
	}
}