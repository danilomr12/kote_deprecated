package br.com.cotecom.model.domain.dtos.inbox
{
import br.com.cotecom.model.utils.ArrayCollections;
import br.com.cotecom.model.utils.EstadoResposta;

import mx.collections.ArrayCollection;
import mx.formatters.DateFormatter;

[RemoteClass(alias="br.com.cotecom.domain.dto.inbox.RespostaItemInboxDTO")]
	public class RespostaItemInboxDTO
	{
		public var id:String;
		public var representante:String;
		public var empresa:String;
		public var status:int;
		public var pedido:ArrayCollection;
		public var falta:ArrayCollection;
        public var dataSalva:Date;

		public function get statusString():String{
			return EstadoResposta.getDescricaoVisaoComprador(this.status)
		}
		
		public function toXml():XML {
			
			var result:XML = new XML( <resposta></resposta> );
			
			result.id = this.id;
            var dateFormatter:DateFormatter = new DateFormatter();
			dateFormatter.formatString = "DD/MM/YY HH:NN";
			result.dataSalva = dateFormatter.format(this.dataSalva);
			result.representante = this.representante;
			result.empresa = this.empresa;
			result.status = this.statusString;
			result.childrem += ArrayCollections.toXml(this.pedido);
			result.childrem += ArrayCollections.toXml(this.falta);
			return result;
		}
		
		public function populate(obj:*):void{
			this.empresa = obj.empresa;
			this.falta = obj.falta;
			this.id = obj.id;
			this.pedido = obj.pedido;
			this.representante = obj.representante;
			this.status = obj.status; 
		}
		
	}
}