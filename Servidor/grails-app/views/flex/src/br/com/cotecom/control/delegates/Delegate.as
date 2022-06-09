package br.com.cotecom.control.delegates{
import br.com.cotecom.model.Session;

import mx.rpc.AsyncToken;
import mx.rpc.IResponder;
import mx.rpc.remoting.RemoteObject;

public class Delegate{
		
		public const SERVICE_FACADE_NAME:String = "remoteService";
		public const SERVICE_CAIXA_DE_ENTRADA_NAME:String = "remoteCaixaDeEntradaService"; 
		
		private var remoteObject:RemoteObject;  
		private var model:Session = Session.getInstance();
		
		public function Delegate() {
			this.remoteObject = new RemoteObject(SERVICE_FACADE_NAME);
		}
			
		public function saveResposta(responder:IResponder,  objectToSave:Object ):void {
			setAsyncToken( this.remoteObject.salveResposta(objectToSave), responder);
		}
		
		public function loadResposta(responder:IResponder, sessionId:String):void {
			setAsyncToken( this.remoteObject.getResposta(sessionId), responder);
		}
		
		public function sendResposta(responder:IResponder, objectToSend:Object):void {
			setAsyncToken( this.remoteObject.envieResposta(objectToSend), responder);
		}
		public function refuseResposta(responder:IResponder, objectToRefuse:Object):void {
			setAsyncToken( this.remoteObject.recuseResposta(objectToRefuse), responder);
		}
				
		public function loadCaixaDeEntrada(responder:IResponder):void {
			this.remoteObject.destination = SERVICE_CAIXA_DE_ENTRADA_NAME;
			setAsyncToken( this.remoteObject.loadCaixaDeEntrada(
				Session.getInstance().user.id), responder);
		}
				
		private function setAsyncToken(object:AsyncToken, responder:IResponder ):void{
			var token:AsyncToken = object;
			token.addResponder(responder);
		}
	}
}