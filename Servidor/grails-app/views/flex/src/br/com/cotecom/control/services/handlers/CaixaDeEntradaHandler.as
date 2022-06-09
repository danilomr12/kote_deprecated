package br.com.cotecom.control.services.handlers
{
import br.com.cotecom.model.domain.dtos.inbox.CotacaoItemInboxDTO;
import br.com.cotecom.model.domain.dtos.inbox.RespostaItemInboxDTO;
import br.com.cotecom.model.domain.dtos.inbox.TelaCaixaDeEntradaDTO;
import br.com.cotecom.model.services.comprador.GerenciadorCaixaDeEntrada;

import flash.events.Event;

import mx.controls.Alert;
import mx.messaging.events.MessageEvent;
import mx.rpc.events.ResultEvent;

public class CaixaDeEntradaHandler implements AsyncMessageHandler{
		[Bindable]
		private var manager:GerenciadorCaixaDeEntrada = GerenciadorCaixaDeEntrada.getInstance();
		
		public function CaixaDeEntradaHandler(){
		}

		public function messageHandler(evt:Event):void {
			var itens:TelaCaixaDeEntradaDTO;
			if(evt is ResultEvent){
				var result1:ResultEvent = evt as ResultEvent;
				itens = result1.result as TelaCaixaDeEntradaDTO;
			}else{
				var result:MessageEvent = evt as MessageEvent;
				itens = result.message.body as TelaCaixaDeEntradaDTO;
			}
			sendToView(itens)
		}
		
		public function sendToView(itens:TelaCaixaDeEntradaDTO):void{
			manager.inBox.removeAll();
			manager.inBoxObject = itens;
			for each(var cotacao:CotacaoItemInboxDTO in itens.itensCaixaDeEntrada){
				for each(var resposta:Object in cotacao.respostas){
					resposta = resposta as RespostaItemInboxDTO
				} 
				manager.inBox.addItem(cotacao.toXml());
			}
		}
		
		public function channelConnectedHandler(evt:Event):void{
			Alert.show(evt.toString())	
		}
		
		public function faultHandler(evt:Event):void{
		//			Alert.show(evt.toString())
		}
		
		public function channelDisconnectedHandler(evt:Event):void{
			Alert.show(evt.toString())
		} 

	}
}