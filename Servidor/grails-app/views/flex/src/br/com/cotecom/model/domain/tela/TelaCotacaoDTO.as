package br.com.cotecom.model.domain.tela
{
import br.com.cotecom.model.domain.cotacao.CotacaoDTO;
import br.com.cotecom.model.domain.cotacao.ItemCotacaoDTO;

import mx.collections.ArrayCollection;

[Bindable]
	[RemoteClass(alias="br.com.cotecom.domain.dto.tela.TelaCotacaoDTO")]
	public class TelaCotacaoDTO {
		
		public var cotacaoDTO:CotacaoDTO;
		public var representantesId:ArrayCollection;
		private var _itensCotacaoDTO:ArrayCollection;
		
		public function TelaCotacaoDTO(cotacaoDTO:CotacaoDTO = null, representantes:ArrayCollection = null, 
			itensCotacaoDTO:ArrayCollection = null) {
				this.cotacaoDTO = (cotacaoDTO) ? cotacaoDTO: new CotacaoDTO();
				this.representantesId = (representantes) ? representantes: new ArrayCollection();
				this._itensCotacaoDTO = (itensCotacaoDTO) ? itensCotacaoDTO: new ArrayCollection();
		}
		
		public function get itensCotacaoDTO():ArrayCollection{
			return this._itensCotacaoDTO;
		} 
		
		public function set itensCotacaoDTO(itens:ArrayCollection):void{
			for each(var item:ItemCotacaoDTO in itens){
				this._itensCotacaoDTO.addItem(item);
			}
		}

	}
}