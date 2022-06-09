package br.com.cotecom.model.domain.dtos.inbox
{
import mx.collections.ArrayCollection;

[RemoteClass(alias='br.com.cotecom.domain.dto.tela.TelaCaixaDeEntradaDTO')]
	public class TelaCaixaDeEntradaDTO{
		
		public var itensCaixaDeEntrada:ArrayCollection;
		
		public function TelaCaixaDeEntradaDTO(itens:ArrayCollection = null){
			this.itensCaixaDeEntrada = (itens)?itensCaixaDeEntrada: new ArrayCollection();
		}
		
		
		public function addCotacao(cotacaoDTO:CotacaoItemInboxDTO):void{
			this.itensCaixaDeEntrada.addItem(cotacaoDTO);
		}
	}
}