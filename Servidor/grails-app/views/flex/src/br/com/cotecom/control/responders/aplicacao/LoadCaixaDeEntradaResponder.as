package br.com.cotecom.control.responders.aplicacao {
import br.com.cotecom.control.services.handlers.CaixaDeEntradaHandler;

import flash.events.Event;

public class LoadCaixaDeEntradaResponder extends ServerResponder {

    public function LoadCaixaDeEntradaResponder(){
        super();
    }

    override public function result(evt:Object):void {
        super.resetCommunication();
        new CaixaDeEntradaHandler().messageHandler(evt as Event);
    }

}
}