package br.com.cotecom.model.domain {
	
	[RemoteClass(alias="br.com.cotecom.domain.dto.usuario.TelefoneDTO")]	
	[Bindable]
	public class Telefone {
		
		public static const RESIDENCIAL:int = 0;
		public static const CELULAR:int = 1;
		public static const COMERCIAL:int = 2;
		public static const FAX:int = 3;
		public static const TIPOS:Array = ["Res", "Cel", "Com", "Fax"];
		
		public var id:*;
        public var ddd:String;
        public var numero:String;
        public var tipo:int;

		public function Telefone( ddd:String = "", numero:String = "", tipo:int = 0, id:* = null) {
			setTipoByIndex(tipo);
			this.numero = numero;
			this.tipo = tipo;
			this.ddd = ddd;
			this.id = id;
		}
		
		public function get descricaoTipo():String{
			return TIPOS[tipo];
		}
		
		public function set descricaoTipo( value:String ):void{
			this.tipo = TIPOS.indexOf(value);
		}
		
		public function setTipoByIndex( value:int ):void{
			this.descricaoTipo = ""+value;
		}
		
		public function equals( telefone:Telefone ):Boolean {
			return 	((this.descricaoTipo == telefone.descricaoTipo)
					&&(this.numero == telefone.numero)
					&&(this.ddd == telefone.ddd))
		}
		
		public function toString():String{
			return "("+this.ddd+") " + this.numero.slice(0,4) + "-" + this.numero.substr(4) + " : " + this.descricaoTipo;
		}
		
		public function get telefoneFormatado():String{
			return this.toString();	
		}
		
		public function clone():Telefone{
			return new Telefone(this.ddd, this.numero, this.tipo);
		}
	}
}