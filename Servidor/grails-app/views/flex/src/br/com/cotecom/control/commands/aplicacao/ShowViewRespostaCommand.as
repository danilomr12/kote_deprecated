package br.com.cotecom.control.commands.aplicacao
{
import br.com.cotecom.control.events.LoadEvent;
import br.com.cotecom.control.events.aplicacao.ShowViewRespostaEvent;
import br.com.cotecom.model.Session;
import br.com.cotecom.model.domain.resposta.TelaResposta;
import br.com.cotecom.model.services.resposta.GerenciadorResposta;
import br.com.cotecom.view.screens.BaseCaixaDeEntrada;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class ShowViewRespostaCommand implements ICommand {

		private var session:Session = Session.getInstance();
		
		public function ShowViewRespostaCommand(){}
		
		public function execute(event:CairngormEvent):void {
			var showViewEvent:ShowViewRespostaEvent = event as ShowViewRespostaEvent;
			session.viewState.applicationViewStack.selectedIndex = 1;
			var baseCxEtda:BaseCaixaDeEntrada = session.viewState.applicationViewStack.selectedChild as BaseCaixaDeEntrada;
			baseCxEtda.selectedIndex = 3;	
			var telaResposta:TelaResposta = GerenciadorResposta.getInstance().telaResposta;
			new LoadEvent(LoadEvent.RESPOSTA, showViewEvent.respostaId).dispatch();
		}
	}
}