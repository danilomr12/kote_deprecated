package br.com.cotecom.model.domain.cotacao
{
	[Bindable]
	[RemoteClass(alias="br.com.cotecom.domain.dto.cotacao.ItemCotacaoDTO")]	
	public class ItemCotacaoDTO	{
		public var id:*;
		public var cotacaoId:*;
		public var produtoId:*;
		public var quantidade:int;
		public var saved:Boolean;
		public var descricao:String;
		public var categoria:String;
        public var qtdMaster:int;
		public var embalagem:String;

		public function ItemCotacaoDTO(cotacaoId:* = null, qtd:int = 0, saved:Boolean = false,
									   	produtoId:* = null, descricao:String = "", emb:String = "", 
										categoria:String = "", qtdMaster:int = 0){
			this.cotacaoId = cotacaoId;
			this.quantidade = qtd;
			this.saved = saved;
			this.produtoId = produtoId;
			this.descricao = descricao;
			this.embalagem = emb;
			this.categoria = categoria;
            this.qtdMaster = qtdMaster;
		}
	}
		
}