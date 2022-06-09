package br.com.cotecom.control.responders.resposta
{
import br.com.cotecom.control.responders.aplicacao.ServerResponder;
import br.com.cotecom.model.Session;
import br.com.cotecom.model.services.resposta.GerenciadorResposta;

import mx.collections.ArrayCollection;
import mx.rpc.events.ResultEvent;

public class LoadRespostasResponder extends ServerResponder{
    public var model:Session = Session.getInstance();
    public var gerenciador:GerenciadorResposta = GerenciadorResposta.getInstance();
    public function LoadRespostasResponder(){}

    override public function result(data:Object):void{
        super.resetCommunication();
        if (hasErrors(data)) return;

        if (!(data.result is ArrayCollection)){
            fault(data.result);
            return;
        }
        var result:ResultEvent = data as ResultEvent;
        var xml:ArrayCollection = new ArrayCollection();
        gerenciador.respostas = result.result as ArrayCollection;
    }
}
}