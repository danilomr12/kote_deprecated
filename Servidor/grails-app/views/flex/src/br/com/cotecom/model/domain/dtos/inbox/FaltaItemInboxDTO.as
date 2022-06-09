package br.com.cotecom.model.domain.dtos.inbox
{
	[RemoteClass(alias="br.com.cotecom.domain.dto.inbox.FaltaItemInboxDTO")]
	public class FaltaItemInboxDTO
	{
		public var id:String;
		public var titulo:String;
		public var qtdeItensFaltantes:int;
		
		public function toXml():XML {
			
			var result:XML = new XML( <falta></falta> )
			
			result.id = this.id
			result.titulo = this.titulo
			result.qtdeItensFaltantes = this.qtdeItensFaltantes
			
			return result;
			
		}
		
	}
}