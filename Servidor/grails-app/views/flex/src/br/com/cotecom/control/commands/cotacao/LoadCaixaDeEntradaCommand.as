package br.com.cotecom.control.commands.cotacao {

import br.com.cotecom.control.delegates.CaixaDeEntradaDelegate;
import br.com.cotecom.control.responders.aplicacao.LoadCaixaDeEntradaResponder;
import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

public class LoadCaixaDeEntradaCommand implements ICommand {

    public function execute(event:CairngormEvent):void {
        var responder:LoadCaixaDeEntradaResponder = new LoadCaixaDeEntradaResponder();
        var delegate:CaixaDeEntradaDelegate = new CaixaDeEntradaDelegate();
        delegate.loadCaixaDeEntrada(responder);
    }
}
}