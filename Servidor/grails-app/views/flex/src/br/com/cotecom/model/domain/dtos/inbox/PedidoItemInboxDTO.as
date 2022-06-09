package br.com.cotecom.model.domain.dtos.inbox
{
	[RemoteClass(alias="br.com.cotecom.domain.dto.inbox.PedidoItemInboxDTO")]
	public class PedidoItemInboxDTO
	{
		public var id:String;
		public var titulo:String;
		public var total:String;
		
		public function toXml():XML {
			
			var result:XML = new XML( <pedido></pedido> )
			
			result.id = this.id
			result.titulo = this.titulo
			result.total = this.total
			
			return result;
			
		}

	}
}