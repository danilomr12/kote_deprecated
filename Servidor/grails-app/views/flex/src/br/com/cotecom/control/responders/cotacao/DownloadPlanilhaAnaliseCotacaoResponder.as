package br.com.cotecom.control.responders.cotacao {
import br.com.cotecom.control.responders.aplicacao.ServerResponder;

import flash.external.ExternalInterface;

import flash.net.URLRequest;
import flash.net.navigateToURL;

import mx.core.Application;

import mx.rpc.events.ResultEvent;
import mx.utils.URLUtil;

public class DownloadPlanilhaAnaliseCotacaoResponder extends ServerResponder{

    public function DownloadPlanilhaAnaliseCotacaoResponder() {
        super();
    }

    override public function result(data:Object):void {
        super.resetCommunication();
        if (hasErrors(data)) return;

        var result:String = (data as ResultEvent).result as String;
        if(result!=null){
            var fullUrl:String = "http://"+URLUtil.getServerNameWithPort(Application.application.loaderInfo.url) +result;
            //var url:URLRequest = new URLRequest(fullUrl);
            //navigateToURL(url, '_blank');
            ExternalInterface.call("window.open",fullUrl);
        }else{
            fault(data)
        }

    }

}
}