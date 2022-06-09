package br.com.cotecom.control.commands.resposta {
import br.com.cotecom.control.delegates.RespostaDelegate;
import br.com.cotecom.control.events.ImporteEvent;
import br.com.cotecom.control.responders.resposta.ImportePlanilhaRespostaResponder;

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class ImportePlanilhaRespostaCommand implements ICommand{

    public function ImportePlanilhaRespostaCommand() {}

    public function execute(event:CairngormEvent):void {
        var importEvent:ImporteEvent = event as ImporteEvent;
        var responder:ImportePlanilhaRespostaResponder = new ImportePlanilhaRespostaResponder ();
        var delegate:RespostaDelegate = new RespostaDelegate();

        delegate.importePlanilhaResposta(responder, importEvent.byteArray, importEvent.params.respostaId,
                importEvent.params.finalizar);
    }
}
}