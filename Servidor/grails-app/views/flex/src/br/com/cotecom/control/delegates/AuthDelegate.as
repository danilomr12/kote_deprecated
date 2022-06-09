package br.com.cotecom.control.delegates
{
import br.com.cotecom.control.responders.usuario.LoginResponder;
import br.com.cotecom.model.Session;

import mx.rpc.AsyncToken;
import mx.rpc.IResponder;
import mx.rpc.remoting.RemoteObject;

public class AuthDelegate
	{
		
		public const SERVICE_FACADE_NAME:String = "remoteAuthService";
		
		private var remoteObject : RemoteObject;
		
		public var model:Session = Session.getInstance();
		
		public function AuthDelegate() {
			this.remoteObject = new RemoteObject(SERVICE_FACADE_NAME);
		}
		
		public function login(responder:LoginResponder, user:String, pass:String):void{
			setAsyncToken(this.remoteObject.doLogin(user, pass), responder);
		}
		
		private function setAsyncToken(object:AsyncToken, responder:IResponder ):void{
			object.addResponder(responder);
		}
	}
}