package br.com.cotecom.control.responders.aplicacao {
import br.com.cotecom.control.errors.ServerError;
import br.com.cotecom.model.Session;

import mx.collections.ArrayCollection;
import mx.core.Application;
import mx.rpc.IResponder;
import mx.rpc.events.FaultEvent;

public class ServerResponder implements IResponder {

    public function ServerResponder() {}

    public function fault(event:Object):void {
        if(Application.application is CompradorMain)
            resetCommunication();
        trace(event.valueOf());
        Session.getInstance().errorHandler.showError(event as FaultEvent);
    }

    public function result(data:Object):void {
        resetCommunication();
    }

    public function hasErrors(data:Object):Boolean {
        if (data.result is ServerError) {
            Session.getInstance().errorHandler.showError(data.result);
            return true;
        }
        if (data.result is ArrayCollection) {
            var dataArray:ArrayCollection = data.result as ArrayCollection;
            if (dataArray.length > 0) {
                if (dataArray.getItemAt(0) is ServerError) {
                    Session.getInstance().errorHandler.showError(data.result);
                    return true;
                }
            }
        }
        return false;
    }

    public function resetCommunication():void {
        Session.getInstance().communicationHandler.resetCommunication();
    }
}
}