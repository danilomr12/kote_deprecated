package br.com.cotecom.control.commands.fornecedores{

import br.com.cotecom.control.delegates.UsuarioDelegate;
import br.com.cotecom.control.events.SearchEvent;
import br.com.cotecom.control.responders.fornecedores.LoadRepresentanteResponder;
import br.com.cotecom.model.services.comprador.GerenciadorRepresentantes;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class SearchRepresentanteCommand implements ICommand{

    public var gerenciador:GerenciadorRepresentantes = GerenciadorRepresentantes.getInstance();

    public function SearchRepresentanteCommand(){}

    public function execute( event : CairngormEvent ) : void{
        var searchEvent:SearchEvent = event as SearchEvent;

        var responder:LoadRepresentanteResponder =
                new LoadRepresentanteResponder(searchEvent.alvo);
        var delegate:UsuarioDelegate = new UsuarioDelegate();
        delegate.searchRepresentantesSemAtendimento(responder, searchEvent.query );
    }
}
}
