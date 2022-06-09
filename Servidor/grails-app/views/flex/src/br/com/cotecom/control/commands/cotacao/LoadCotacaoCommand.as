package br.com.cotecom.control.commands.cotacao{

import br.com.cotecom.control.delegates.CotacaoDelegate;
import br.com.cotecom.control.events.LoadEvent;
import br.com.cotecom.control.responders.cotacao.LoadCotacaoResponder;
import br.com.cotecom.model.domain.cotacao.CotacaoDTO;
import br.com.cotecom.model.services.comprador.GerenciadorCotacao;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class LoadCotacaoCommand implements ICommand{
		
		public var gerenciador:GerenciadorCotacao = GerenciadorCotacao.getInstance();
		public var cotacaoSelecionada:CotacaoDTO = gerenciador.cotacaoSelecionada;
		
		public function LoadCotacaoCommand(){}
 
		public function execute( event : CairngormEvent ):void{
			var loadEvent:LoadEvent = event as LoadEvent;
			
			var responder:LoadCotacaoResponder = new LoadCotacaoResponder();
			var delegate:CotacaoDelegate = new CotacaoDelegate();
			
			delegate.loadCotacao(responder, loadEvent.id);
		}
	}
}
