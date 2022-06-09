package br.com.cotecom.control.commands.usuario
{
import br.com.cotecom.control.delegates.AuthDelegate;
import br.com.cotecom.control.events.usuario.LoginEvent;
import br.com.cotecom.control.responders.usuario.LoginResponder;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class LoginCommand implements ICommand
	{
		public function LoginCommand(){
		}
		
		public function execute(event:CairngormEvent):void {
			var loginEvent:LoginEvent = event as LoginEvent;
			var loginResponder:LoginResponder = new LoginResponder(loginEvent.popup);
			var delegate:AuthDelegate = new AuthDelegate();
			delegate.login(loginResponder, loginEvent.user, loginEvent.pass);
		}
	}
}