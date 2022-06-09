package br.com.cotecom.control.responders.fornecedores{
import br.com.cotecom.control.responders.aplicacao.ServerResponder;

import mx.collections.ArrayCollection;
import mx.rpc.events.ResultEvent;

public class LoadRepresentanteResponder extends ServerResponder{

    public var target:ArrayCollection;

    public function LoadRepresentanteResponder(target:ArrayCollection) {
        super();
        this.target = target;
    }

    override public function result(data:Object):void {
        super.resetCommunication();
        if (hasErrors(data)) return;

        var evento:ResultEvent = data as ResultEvent;
        var representanteDTOs:ArrayCollection = evento.result as ArrayCollection;

        if(representanteDTOs != null||representanteDTOs.length>0){
            this.target.removeAll();
            this.target.addAll(representanteDTOs);
        }
    }

}
}
