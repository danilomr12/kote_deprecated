package br.com.cotecom.control.services
{
import br.com.cotecom.control.events.LoadEvent;
import br.com.cotecom.control.services.handlers.AsyncMessageHandler;
import br.com.cotecom.control.services.handlers.CaixaDeEntradaHandler;
import br.com.cotecom.model.Session;

public class CaixaDeEntradaService
	{
		
		public var session:Session;
		//public var consumer:Consumer;
		public var handler:AsyncMessageHandler;
		
		public function CaixaDeEntradaService() {
			session = Session.getInstance();
			handler = new CaixaDeEntradaHandler();
		}
		 
		/*public function cadastrarCaixaDeEntrada():void {
			consumer = new Consumer();
			consumer.destination = "caixaDeEntradaTopic";
			consumer.selector = "userID='" + session.user.id + "'";
			consumer.addEventListener(ChannelEvent.DISCONNECT, handler.channelDisconnectedHandler);
			consumer.addEventListener(ChannelEvent.CONNECT, handler.channelConnectedHandler);
			consumer.addEventListener(MessageEvent.MESSAGE, handler.messageHandler);
			consumer.addEventListener(MessageFaultEvent.FAULT, handler.faultHandler);
			consumer.subscribe("userID='" + session.user.id + "'");
		} 
		*/
		public function getAllItens():void {
			var event:LoadEvent = new LoadEvent(LoadEvent.CAIXA_DE_ENTRADA);
			event.dispatch();
			
		}

	}
}