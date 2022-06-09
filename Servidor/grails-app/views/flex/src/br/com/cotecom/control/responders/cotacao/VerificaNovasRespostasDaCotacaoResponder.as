package br.com.cotecom.control.responders.cotacao {
import br.com.cotecom.control.responders.aplicacao.ServerResponder;
import br.com.cotecom.model.services.comprador.GerenciadorCotacao;

import mx.rpc.events.ResultEvent;

public class VerificaNovasRespostasDaCotacaoResponder extends ServerResponder{

    private var gerenciadorCotacao:GerenciadorCotacao = GerenciadorCotacao.getInstance();

    public function VerificaNovasRespostasDaCotacaoResponder() {
        super();
    }


    override public function fault(event:Object):void {
        gerenciadorCotacao.resetTimerAnalise();
        super.fault(event);
    }

    override public function result(data:Object):void {
        super.resetCommunication();
        if(data.result != null){
            var possuiRespostas:Boolean = (data as ResultEvent).result as Boolean;
            if(possuiRespostas){
                gerenciadorCotacao.crieNotificacaoNovaRespostaDaCotacao()
            }else{
                gerenciadorCotacao.resetTimerAnalise();
            }
        } else {
            fault(data);
        }
    }

}
}
