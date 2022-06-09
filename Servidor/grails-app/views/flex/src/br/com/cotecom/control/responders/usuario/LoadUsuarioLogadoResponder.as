package br.com.cotecom.control.responders.usuario {
import br.com.cotecom.control.events.LoadEvent;
import br.com.cotecom.control.services.CaixaDeEntradaService;
import br.com.cotecom.model.Session;
import br.com.cotecom.model.domain.dtos.usuarios.TipoUsuario;
import br.com.cotecom.model.domain.dtos.usuarios.Usuario;
import br.com.cotecom.model.services.resposta.GerenciadorFornecedores;

import mx.rpc.IResponder;
import mx.rpc.events.FaultEvent;
import mx.rpc.events.ResultEvent;

public class LoadUsuarioLogadoResponder implements IResponder {
    public var model:Session = Session.getInstance();
    public var target:Usuario;

    public function LoadUsuarioLogadoResponder(target:*) {
        this.target = target;
    }

    public function result(event:Object):void {
        var evento:ResultEvent = event as ResultEvent;
        var usuario:* = evento.result;
        if (usuario == null)
            fault(event);
        model.user = usuario;

        var loadEvent:LoadEvent;
        if(model.user.tipo != TipoUsuario.REPRESENTANTE){
            //Load Caixa De Entrada Apos a Requisicao do Usuario Logado
            model.caixaDeEntradaService = new CaixaDeEntradaService();
            model.caixaDeEntradaService.getAllItens();
            if(model.user.tipo == TipoUsuario.COMPRADOR){
                loadEvent = new LoadEvent(LoadEvent.REPRESENTANTES);
                loadEvent.dispatch();
            }
        }else if(model.user.tipo == TipoUsuario.REPRESENTANTE){
            new LoadEvent(LoadEvent.RESPOSTAS).dispatch();
            GerenciadorFornecedores.getInstance().loadInformacoes();
        }
    }

    public function fault(event:Object):void {
        model.errorHandler.showError(event as FaultEvent);
        trace(event.valueOf());
    }

}
}
