package br.com.cotecom.control.controllers{

import br.com.cotecom.control.commands.aplicacao.ShowViewCaixaDeEntradaCommand;
import br.com.cotecom.control.commands.aplicacao.ShowViewCotacaoRascunhoCommand;
import br.com.cotecom.control.events.aplicacao.ShowViewCaixaDeEntradaEvent;
import br.com.cotecom.control.events.aplicacao.ShowViewCotacaoRascunhoEvent;

import com.adobe.cairngorm.control.FrontController;

public class EstoquistaController extends FrontController{

    public function EstoquistaController(){
        initialiseCommands();
    }

    private function initialiseCommands() : void{
        addCommand(ShowViewCotacaoRascunhoEvent.EVENT_NAME, ShowViewCotacaoRascunhoCommand);
        addCommand(ShowViewCaixaDeEntradaEvent.EVENT_NAME, ShowViewCaixaDeEntradaCommand);
    }
}
}
