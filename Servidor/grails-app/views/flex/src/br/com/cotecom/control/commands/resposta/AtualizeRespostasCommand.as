package br.com.cotecom.control.commands.resposta {
import br.com.cotecom.control.delegates.CotacaoDelegate;
import br.com.cotecom.control.events.cotacao.AtualizeRespostasEvent;
import br.com.cotecom.control.responders.cotacao.PopulateAnaliseCotacaoResponder;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

import mx.rpc.IResponder;

public class AtualizeRespostasCommand implements ICommand {

    public function AtualizeRespostasCommand() {
    }

    public function execute(event:CairngormEvent):void {
        var qtddRespostasConcluidas:int = (event as AtualizeRespostasEvent).qtddRespostasConcluidas;
        var idCotacao:* = (event as AtualizeRespostasEvent).idCotacao;
        var responder:IResponder = new PopulateAnaliseCotacaoResponder();
        var delegate:CotacaoDelegate = new CotacaoDelegate();
        delegate.atualizeRespostas(responder, qtddRespostasConcluidas, idCotacao);
    }
}
}
