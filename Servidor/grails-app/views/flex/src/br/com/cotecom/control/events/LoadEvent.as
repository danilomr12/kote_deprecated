package br.com.cotecom.control.events{

import com.adobe.cairngorm.control.CairngormEvent;

import flash.events.Event;

public class LoadEvent extends CairngormEvent{
		
		public static const REPRESENTANTES : String = "LoadRepresentante";
		public static const COTACAO : String = "LoadCotacao";
		public static const RESPOSTA : String = "LoadResposta";
		public static const CAIXA_DE_ENTRADA: String = "LoadCaixaDeEntrada";
		public static const USUARIO_LOGADO: String = "LoadUsuarioLogado";
		public static const ANALISE_COTACAO : String = "LoadAnaliseCotacao";
		public static const EMPRESA_CLIENTE : String = "LoadEmpresaCliente";
		public static const EMPRESA_FORNECEDOR : String = "LoadEmpresaFornecedor";
		public static const SUPERVISOR : String = "LoadSupervisor";
        public static const RESPOSTAS:String = "LoadRespostas";
        public static const PEDIDO:String = "loadPedido";
        public var id:*;

		public function LoadEvent (type:String, id:* = null,target:* = null) {
			super(type);
			this.id = id;
		}
		
		public override function clone() : Event {
			return new LoadEvent (this.type , this.id);
		}
	}
}
