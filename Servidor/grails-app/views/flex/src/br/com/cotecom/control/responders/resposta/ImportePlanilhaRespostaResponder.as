package br.com.cotecom.control.responders.resposta {

import br.com.cotecom.control.responders.aplicacao.ServerResponder;
import br.com.cotecom.model.Session;
import br.com.cotecom.model.domain.resposta.TelaResposta;
import br.com.cotecom.model.services.resposta.GerenciadorResposta;
import br.com.cotecom.model.utils.EstadoResposta;
import br.com.cotecom.view.util.Icons;

import mx.rpc.events.ResultEvent;

public class ImportePlanilhaRespostaResponder extends ServerResponder{

    public var model:Session = Session.getInstance();
    public var gerenciador:GerenciadorResposta = GerenciadorResposta.getInstance();

    public function ImportePlanilhaRespostaResponder() {
        super();
    }

    override public function result(data:Object):void {
        super.resetCommunication();
        if (hasErrors(data)) return;
        var result:TelaResposta = (data as ResultEvent).result as TelaResposta;
        if(result!=null){
            gerenciador.telaResposta.resposta.dataSalva = result.resposta.dataSalva;
            gerenciador.telaResposta.resposta.status = result.resposta.status;
            gerenciador.setAllItensAsSaved();
            gerenciador.telaResposta.itensResposta = result.itensResposta;
            gerenciador.editavel = result.resposta.status == EstadoResposta.RESPONDENDO;
            model.messageHandler.showTextMessage("Resposta", "Resposta Importada com sucesso", Icons.SUCESSFUL_24);
        }else{
            fault(data)
        }
    }
}
}