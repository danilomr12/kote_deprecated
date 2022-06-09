package br.com.cotecom.control.responders.cotacao {
import br.com.cotecom.control.responders.aplicacao.ServerResponder;
import br.com.cotecom.model.services.comprador.GerenciadorCaixaDeEntrada;

import mx.rpc.events.ResultEvent;

public class VerificaNovasRespostasResponder extends ServerResponder{

    private var gerenciadorCxEntrada:GerenciadorCaixaDeEntrada = GerenciadorCaixaDeEntrada.getInstance();

    public function VerificaNovasRespostasResponder() {
        super();
    }

    override public function fault(event:Object):void {
        gerenciadorCxEntrada.resetTimerAtualizacaoCxEntrada();
        super.fault(event);
    }

    override public function result(data:Object):void {
        super.resetCommunication();
        if(data.result != null){
            var possuiRespostas:Boolean = (data as ResultEvent).result as Boolean;
            if(possuiRespostas){
                GerenciadorCaixaDeEntrada.getInstance().crieNotificacaoNovaResposta();
            }else{
                gerenciadorCxEntrada.resetTimerAtualizacaoCxEntrada();
            }
        } else {
            fault(data);
        }
    }

}
}
