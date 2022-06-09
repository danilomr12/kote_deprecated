package br.com.cotecom.control.responders.catalogo {
import br.com.cotecom.control.responders.aplicacao.ServerResponder;
import br.com.cotecom.model.Session;

import flash.external.ExternalInterface;

import flash.net.FileReference;
import flash.net.URLRequest;
import flash.net.navigateToURL;

import mx.core.Application;

import mx.rpc.events.ResultEvent;
import mx.utils.URLUtil;

public class DownloadPlanilhaExemploResponder extends ServerResponder {

    public var model:Session = Session.getInstance();
    public var arquivo:Object;
    private var fileRef:FileReference;

    public function DownloadPlanilhaExemploResponder() {
        super();
    }

    override public function result(event:Object):void {
        super.resetCommunication();
        if (hasErrors(event)) return;
        var result:String = (event as ResultEvent).result as String;
        if (result != null) {
            var fullUrl:String = "http://"+URLUtil.getServerNameWithPort(Application.application.loaderInfo.url) + result;
            //var url:URLRequest = new URLRequest(fullUrl);
            //navigateToURL(url, '_blank');
            ExternalInterface.call("window.open",fullUrl);
        } else {
            fault(event)
        }
    }

}
}