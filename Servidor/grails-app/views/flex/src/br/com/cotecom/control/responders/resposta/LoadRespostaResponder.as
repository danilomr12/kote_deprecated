package br.com.cotecom.control.responders.resposta{
import br.com.cotecom.control.responders.aplicacao.ServerResponder;
import br.com.cotecom.model.Session;
import br.com.cotecom.model.domain.dtos.usuarios.TipoUsuario;
import br.com.cotecom.model.domain.resposta.TelaResposta;
import br.com.cotecom.model.services.resposta.GerenciadorResposta;
import br.com.cotecom.model.utils.EstadoResposta;
import br.com.cotecom.view.components.resposta.TermoAcordo;

import flash.display.DisplayObject;

import mx.collections.ArrayCollection;
import mx.core.Application;
import mx.managers.PopUpManager;
import mx.rpc.events.ResultEvent;

public class LoadRespostaResponder extends ServerResponder {

    public var gerenciador:GerenciadorResposta = GerenciadorResposta.getInstance();

    public function LoadRespostaResponder() {
        super();
    }

    override public function result(event:Object):void {
        super.resetCommunication();
        if (hasErrors(event)) return;

        var evento:ResultEvent = event as ResultEvent;
        if (event.result is ArrayCollection){
            fault(event.result);
            return;
        }

        gerenciador.telaResposta = evento.result as TelaResposta;

        if(Session.getInstance().user.tipo == TipoUsuario.REPRESENTANTE && gerenciador.telaResposta.resposta.status == EstadoResposta.NOVA_COTACAO){
            var janelaTermoAcordo:TermoAcordo = new TermoAcordo();
            janelaTermoAcordo.showCloseButton = true;
            janelaTermoAcordo.telaResposta = gerenciador.telaResposta;
            PopUpManager.addPopUp(janelaTermoAcordo, Application.application as DisplayObject,true);
            PopUpManager.centerPopUp(janelaTermoAcordo);
        }
        if(Session.getInstance().user.tipo == TipoUsuario.REPRESENTANTE && gerenciador.telaResposta.resposta.status == EstadoResposta.RESPONDENDO){
            gerenciador.editavel = true;
        }

    }

}
}
