package br.com.cotecom.control.responders.cotacao
{
import br.com.cotecom.control.events.cotacao.AdicioneItemCotacaoEvent;
import br.com.cotecom.control.responders.aplicacao.ServerResponder;
import br.com.cotecom.model.Session;
import br.com.cotecom.model.domain.cotacao.ItemCotacaoDTO;
import br.com.cotecom.model.domain.dtos.Produto;
import br.com.cotecom.model.services.comprador.GerenciadorCotacao;

import mx.collections.ArrayCollection;
import mx.rpc.events.ResultEvent;

public class AdicioneProdutoACotacaoResponder extends ServerResponder
	{
		public var alvo:ArrayCollection;
		public var quantidade:int; 
		private var session:Session = Session.getInstance();
		private var gerenciador:GerenciadorCotacao = GerenciadorCotacao.getInstance();
		
		public function AdicioneProdutoACotacaoResponder(alvo:ArrayCollection, quantidade:int) {
			super();
            this.alvo = alvo;
			this.quantidade = quantidade;
		}
		
		override public function result( event:Object ):void {
            super.resetCommunication();
            if (hasErrors(event)) return;

            var evento:ResultEvent = event as ResultEvent;
			var listaProdutos:ArrayCollection = evento.result as ArrayCollection;
            var itensCotacaoTemp:ArrayCollection = new ArrayCollection();

			for each(var item:Produto in listaProdutos){		
				itensCotacaoTemp.addItem(new ItemCotacaoDTO(gerenciador.telaCotacaoDTO.cotacaoDTO.id, quantidade, false,
					item.id, item.descricao, item.embalagem, item.categoria, item.qtdMaster));
			}
            new AdicioneItemCotacaoEvent(itensCotacaoTemp).dispatch();
		}
		
	}
}