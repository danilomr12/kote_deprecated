package br.com.cotecom.control.commands.cotacao {
import br.com.cotecom.control.delegates.CotacaoDelegate;
import br.com.cotecom.control.events.cotacao.VerificaNovasRespostasEvent;
import br.com.cotecom.control.responders.cotacao.VerificaNovasRespostasDaCotacaoResponder;
import br.com.cotecom.control.responders.cotacao.VerificaNovasRespostasResponder;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class VerificaNovasRespostasCommand implements ICommand{

    public function VerificaNovasRespostasCommand() {}

    public function execute(event:CairngormEvent):void {
        var verificaNovasRespostasEvent:VerificaNovasRespostasEvent = event as VerificaNovasRespostasEvent;
        var delegate:CotacaoDelegate = new CotacaoDelegate();

        if(verificaNovasRespostasEvent.cotacaoId > 0){
            var responder2:VerificaNovasRespostasDaCotacaoResponder = new VerificaNovasRespostasDaCotacaoResponder();
            delegate.possuiNovasRespostasDaCotacao(responder2, verificaNovasRespostasEvent.cotacaoId);
        }else {
            var responder1:VerificaNovasRespostasResponder = new VerificaNovasRespostasResponder();
            delegate.possuiNovasRespostas(responder1);
        }
    }
}
}
