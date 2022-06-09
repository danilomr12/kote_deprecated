package br.com.cotecom.control.controllers{

import br.com.cotecom.control.commands.aplicacao.InitializeCommand;
import br.com.cotecom.control.commands.cotacao.LoadCaixaDeEntradaCommand;
import br.com.cotecom.control.commands.usuario.LoadUsuarioLogadoCommand;
import br.com.cotecom.control.commands.aplicacao.StatusMessageChangeCommand;
import br.com.cotecom.control.events.InitializeEvent;
import br.com.cotecom.control.events.LoadEvent;
import br.com.cotecom.control.events.LoadEvent;
import br.com.cotecom.control.events.StatusMessageChangeEvent;

import com.adobe.cairngorm.control.FrontController;

public class ApplicationController extends FrontController{

    public function ApplicationController(){
        initialiseCommands();
    }

    private function initialiseCommands() : void{
        addCommand(InitializeEvent.NAME, InitializeCommand);
        addCommand(StatusMessageChangeEvent.NAME, StatusMessageChangeCommand);
        addCommand(LoadEvent.USUARIO_LOGADO, LoadUsuarioLogadoCommand);
        addCommand(LoadEvent.CAIXA_DE_ENTRADA, LoadCaixaDeEntradaCommand);
    }
}
}
