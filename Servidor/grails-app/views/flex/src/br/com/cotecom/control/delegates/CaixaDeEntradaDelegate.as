package br.com.cotecom.control.delegates{
import br.com.cotecom.model.Session;

import mx.rpc.AsyncToken;
import mx.rpc.IResponder;
import mx.rpc.remoting.RemoteObject;

public class CaixaDeEntradaDelegate{
		
		public const SERVICE_FACADE_NAME:String = "remoteCaixaDeEntradaService"; 
		
		private var remoteObject:RemoteObject;  
		private var model:Session = Session.getInstance();
		
		public function CaixaDeEntradaDelegate() {
			this.remoteObject = new RemoteObject(SERVICE_FACADE_NAME);
            this.remoteObject.showBusyCursor = true;
		}
		
		public function loadCaixaDeEntrada(responder:IResponder):void {
			setAsyncToken( this.remoteObject.loadCaixaDeEntrada(
				Session.getInstance().user), responder);
		}
		
		private function setAsyncToken(object:AsyncToken, responder:IResponder ):void{
            object.addResponder(responder);
		}
	}
}