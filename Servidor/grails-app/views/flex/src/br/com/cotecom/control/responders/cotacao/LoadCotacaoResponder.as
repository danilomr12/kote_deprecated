package br.com.cotecom.control.responders.cotacao
{
import br.com.cotecom.control.responders.aplicacao.ServerResponder;
import br.com.cotecom.model.Session;
import br.com.cotecom.model.domain.tela.TelaCotacaoDTO;
import br.com.cotecom.model.services.comprador.GerenciadorCotacao;

import mx.rpc.events.ResultEvent;

public class LoadCotacaoResponder extends ServerResponder {
	
		private var model:Session = Session.getInstance();
		private var gerenciadorCotacao:GerenciadorCotacao = GerenciadorCotacao.getInstance();
			
		public function LoadCotacaoResponder() {
            super();
        }
		
		override public function result(data:Object):void {
			super.resetCommunication();
            if (hasErrors(data)) return;

            var evento:ResultEvent = data as ResultEvent;
			if(data==null){
				fault(data);
			}
			var telaCotacaoDTO:TelaCotacaoDTO = evento.result as TelaCotacaoDTO;
			
			if(telaCotacaoDTO == null){
				fault(data);
			} else {
				gerenciadorCotacao.telaCotacaoDTO = telaCotacaoDTO;
				gerenciadorCotacao.cotacaoSelecionada = telaCotacaoDTO.cotacaoDTO;
			}

		}

	}
}