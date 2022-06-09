package br.com.cotecom.control.delegates{

import br.com.cotecom.model.Session;
import br.com.cotecom.model.domain.dtos.Produto;

import flash.utils.ByteArray;

import mx.rpc.AsyncToken;
import mx.rpc.IResponder;
import mx.rpc.remoting.RemoteObject;

public class ProdutoDelegate{
		
		private const SERVICE_FACADE_NAME:String = "remoteProdutoService";
		private var remoteObject : RemoteObject;
		
		public var model:Session = Session.getInstance();
		
		public function ProdutoDelegate() {
			this.remoteObject = new RemoteObject( SERVICE_FACADE_NAME );
            this.remoteObject.showBusyCursor = true;
		}
		
		public function searchProduto(responder:IResponder, query:String ):void {
			setAsyncToken( this.remoteObject.search( query ), responder);
		}
		
		public function removeProduto(responder:IResponder,  objectToRemove:Produto):void {
			setAsyncToken( this.remoteObject.remove(objectToRemove), responder);
		}
		
		public function saveProduto(responder:IResponder,  objectToSave:Object ):void {
			setAsyncToken( this.remoteObject.saveProduto(objectToSave as Produto), responder);
		}

		public function adicioneProdutoACotacao(responder:IResponder, produto:Produto):void{
			setAsyncToken( this.remoteObject.saveProdutos([produto]), responder);
		}
		
		public function downloadPlanilhaExemploImportacao(responder:IResponder):void{
			setAsyncToken( this.remoteObject.donwloadPlanilhaExemplo(), responder);
		}
		
		public function importePlanilhaProdutos(responder:IResponder, bytes:ByteArray):void{
			setAsyncToken( this.remoteObject.importePlanilhaProdutos(bytes), responder);
		}
		
		private function setAsyncToken(object:AsyncToken, responder:IResponder ):void{
            object.addResponder(responder);
		}
	}
}