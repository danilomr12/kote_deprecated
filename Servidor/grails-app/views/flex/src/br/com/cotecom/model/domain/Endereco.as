package br.com.cotecom.model.domain{	

	[RemoteClass(alias="br.com.cotecom.domain.dto.usuario.EnderecoDTO")]
	[Bindable]
	public class Endereco{
	
		public var id:*;
        public var version:*;

		public var logradouro:String;
		public var bairro:String;
		public var cidade:String;
    	public var estado:String;
    	public var numero:String;
		public var complemento:String;
		public var cep:String;
		
		public function Endereco(id:* = null, logradouro:String = "", cidade:String = "", estado:String ="", 
								 numero:String= undefined, complemento:String = "", bairro:String = "", cep:String = ""){
			this.id = id;
			this.logradouro = logradouro;
			this.cidade = cidade;
			this.estado = estado;
			this.numero = numero;
			this.bairro = bairro;
			this.complemento = complemento;
			this.cep = cep;
		}
		
		public function equals(outro:Endereco):Boolean{
			if(outro && (this.logradouro == outro.logradouro)){
				return true;
			}
			return false;
		}

        public function clone():Endereco {
            return new Endereco(this.id, this.logradouro, this.cidade, this.estado, this.numero, this.complemento,
                    this.bairro, this.cep)
        }

	}
}