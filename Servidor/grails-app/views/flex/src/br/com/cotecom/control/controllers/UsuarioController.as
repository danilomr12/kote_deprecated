package br.com.cotecom.control.controllers{

import br.com.cotecom.control.commands.usuario.SaveCompradorCommand;
import br.com.cotecom.control.commands.fornecedores.SaveEmpresaCommand;
import br.com.cotecom.control.commands.usuario.LoginCommand;
import br.com.cotecom.control.events.SaveEvent;
import br.com.cotecom.control.events.usuario.LoginEvent;

import com.adobe.cairngorm.control.FrontController;

public class UsuarioController extends FrontController{

    public function UsuarioController(){
        initialiseCommands();
    }

    private function initialiseCommands() : void{
        addCommand(SaveEvent.COMPRADOR, SaveCompradorCommand);
		addCommand(LoginEvent.EVENT_NAME, LoginCommand);
        addCommand(SaveEvent.EMPRESA_CLIENTE, SaveEmpresaCommand);
    }
}
}
