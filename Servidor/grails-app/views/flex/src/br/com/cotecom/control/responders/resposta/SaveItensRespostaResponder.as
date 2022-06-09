package br.com.cotecom.control.responders.resposta{
import br.com.cotecom.control.responders.aplicacao.ServerResponder;
import br.com.cotecom.model.Session;
import br.com.cotecom.model.services.resposta.GerenciadorResposta;
import br.com.cotecom.view.util.Icons;

import mx.collections.ArrayCollection;
import mx.rpc.events.ResultEvent;

public class SaveItensRespostaResponder extends ServerResponder{

    public var itensASeremSalvos:ArrayCollection;
    private var gerenciador:GerenciadorResposta = GerenciadorResposta.getInstance();

    public function SaveItensRespostaResponder(itens:ArrayCollection) {
        super();
        this.itensASeremSalvos = itens;
    }

    override public function fault(event:Object):void {
        gerenciador.resetTimerSalvamento();
        super.fault(event);
    }

    override public function result( event:Object ):void {
        super.resetCommunication();
        if (hasErrors(event)) return;
        var resultEvento:ResultEvent = event as ResultEvent;
        var dataSalva:Date;
        if(resultEvento.result is Date)
            dataSalva = resultEvento.result as Date;
        if(dataSalva != null){
            gerenciador.telaResposta.setItensAsSaved(itensASeremSalvos);
            gerenciador.resetTimerSalvamento();
            gerenciador.telaResposta.alterada = false;
            gerenciador.telaResposta.resposta.dataSalva = dataSalva;
            gerenciador.telaResposta.resposta.dataSalvaFormatada = "";
        }else{
            Session.getInstance().messageHandler.showTextMessage("Erro","Sua resposta não está sendo salva corretamente. " +
                    "Verifique sua conexão com internet. Ou contate o administrador" , Icons.ERROR);
        }
    }
}
}
