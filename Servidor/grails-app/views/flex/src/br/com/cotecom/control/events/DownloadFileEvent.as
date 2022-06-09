package br.com.cotecom.control.events
{
import com.adobe.cairngorm.control.CairngormEvent;

import flash.events.Event;

public class DownloadFileEvent extends CairngormEvent{

		public var tipo:String;
        public var params:Object;
		public static const PLANILHA_IMPORTACAO_PRODUTOS_EXEMPLO:String = "downloadPlanilhaExemplo";
        public static const PLANILHA_ANALISE_COTACAO:String = "downloadPlanilhaAnaliseCotacao";
        public static const PLANILHA_COTACAO_RESPOSTA_OFF_LINE:String = "downloadPlanilhaResposta";
        public static const PEDIDO:String = "downloadPedido";

		public function DownloadFileEvent(tipo:String){
			super(tipo);
		}
		
		public override function clone() : Event{
			return new DownloadFileEvent( this.tipo);
		}
	}
	

}