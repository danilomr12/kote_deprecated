package br.com.cotecom.control.delegates{
import br.com.cotecom.model.Session;

import flash.utils.ByteArray;

import mx.rpc.AsyncToken;
import mx.rpc.IResponder;
import mx.rpc.remoting.RemoteObject;

public class RespostaDelegate{

    public const SERVICE_FACADE_NAME:String = "remoteRespostaService";

    private var remoteObject:RemoteObject;

    public var model:Session = Session.getInstance();

    public function RespostaDelegate() {
        this.remoteObject = new RemoteObject(SERVICE_FACADE_NAME);
        this.remoteObject.showBusyCursor = true;
    }

    public function saveResposta(responder:IResponder,  objectToSave:Object ):void {
        setAsyncToken( this.remoteObject.salveItemResposta(objectToSave), responder);
    }

    public function loadRespostas(responder:IResponder):void {
        setAsyncToken( this.remoteObject.getRespostas(), responder);
    }

    public function loadRespostaById(responder:IResponder, respostaId:int):void {
        setAsyncToken( this.remoteObject.getRespostaById(respostaId), responder);
    }

    public function sendResposta(responder:IResponder, objectToSend:Object):void {
        setAsyncToken( this.remoteObject.envieResposta(objectToSend), responder);
    }

    public function recuseResposta(responder:IResponder, objectToRefuse:Object):void {
        setAsyncToken( this.remoteObject.recuseResposta(objectToRefuse), responder);
    }

    public function acceptResposta(responder:IResponder, objectToAccept:Object):void {
        setAsyncToken( this.remoteObject.aceiteResposta(objectToAccept), responder);
    }

    public function downloadPlanilhaResposta(responder:IResponder, respostaId:Object):void {
        setAsyncToken(this.remoteObject.downloadFormularioCotacaoParaRespostaOffLine(respostaId),responder)
    }

    public function importePlanilhaResposta(responder:IResponder, byteArray:ByteArray, respostaId:*, finalizar:Boolean):void {
        setAsyncToken(this.remoteObject.importePlanilhaResposta(byteArray, respostaId, finalizar),responder)
    }

    public function canceleResposta(responder:IResponder, respostaId:Number):void {
        setAsyncToken(this.remoteObject.canceleResposta(respostaId), responder)
    }

    public function loadPedidoById(responder:IResponder, pedidoId:int):void {
        setAsyncToken( this.remoteObject.getPedidoByid(pedidoId), responder);
    }

    public function ressusciteResposta(responder:IResponder, respostaId:*):void {
        setAsyncToken(this.remoteObject.ressusciteResposta(respostaId), responder)
    }
    public function faturePedido(responder:IResponder, pedidoId:*):void {
        setAsyncToken(this.remoteObject.faturePedido(pedidoId), responder)
    }

    public function downloadPedido(responder:IResponder, idPedido:*):void {
        setAsyncToken(this.remoteObject.downloadPedido(idPedido), responder);
    }

    private function setAsyncToken(object:AsyncToken, responder:IResponder ):void{
        object.addResponder(responder);
    }

}
}