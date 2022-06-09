package br.com.cotecom.model.domain.dtos.inbox
{
import br.com.cotecom.model.utils.ArrayCollections;

import mx.collections.ArrayCollection;
import mx.formatters.DateFormatter;

[RemoteClass(alias="br.com.cotecom.domain.dto.inbox.CotacaoItemInboxDTO")]
	public class CotacaoItemInboxDTO{
	
		public var id:String;
		public var nome:String;
		public var estado:String;
		public var dataSalva:Date;
        public var dataCriacao:Date;
		public var titulo:String;
		public var estadoCount:String;
		public var respostas:ArrayCollection;
		
		public function toXml():XML {
			var result:XML = new XML( <cotacao></cotacao> );
	
			var dateFormatter:DateFormatter = new DateFormatter();
			dateFormatter.formatString = "DD/MM/YY HH:NN";
			result.dataSalva = dateFormatter.format(this.dataSalva);
            result.dataCriacao = dateFormatter.format(this.dataCriacao);
			result.id = this.id;
			result.nome = this.nome;
			result.status = this.estado;
			result.titulo = this.titulo;
			result.estadoCount = this.estadoCount;
			result.respostas = ArrayCollections.toXml(this.respostas);
			
			return result;
		}
		
	}
}