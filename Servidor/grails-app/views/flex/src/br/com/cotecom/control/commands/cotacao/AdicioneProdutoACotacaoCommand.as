package br.com.cotecom.control.commands.cotacao
{
import br.com.cotecom.control.delegates.ProdutoDelegate;
import br.com.cotecom.control.events.cotacao.AdicioneProdutoACotacaoEvent;
import br.com.cotecom.control.responders.cotacao.AdicioneProdutoACotacaoResponder;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class AdicioneProdutoACotacaoCommand implements ICommand{
		
		public function AdicioneProdutoACotacaoCommand(){}
		
		public function execute(event: CairngormEvent):void{
			var addEvent:AdicioneProdutoACotacaoEvent = event as AdicioneProdutoACotacaoEvent;
			
			var responder:AdicioneProdutoACotacaoResponder = 
				new AdicioneProdutoACotacaoResponder(addEvent.alvo, addEvent.quantidade);
			
			var delegate:ProdutoDelegate = new ProdutoDelegate();
			delegate.adicioneProdutoACotacao(responder, addEvent.produto); 
		}
	}
}