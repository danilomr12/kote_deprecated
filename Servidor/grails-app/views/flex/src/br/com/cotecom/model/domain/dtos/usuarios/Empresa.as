package br.com.cotecom.model.domain.dtos.usuarios{
import br.com.cotecom.model.domain.Endereco;
import br.com.cotecom.model.domain.Telefone;

import mx.collections.ArrayCollection;

[RemoteClass(alias="br.com.cotecom.domain.dto.usuario.EmpresaDTO")]
    [Bindable]
	public class Empresa{

		public var id:*;
        public var version:*;
		public var nomeFantasia:String;
		public var razaoSocial:String;
		public var email:String;
		public var cnpj:String;
		public var telefones:ArrayCollection;
		public var endereco:Endereco;
        public var tipo:int = 0;

        public static const CLIENTE:int = 0;
        public static const FORNECEDOR:int = 1;

	public function Empresa(tipo:int = 0, nomeFantasia:String = "", razaoSocial:String = "", email:String = "",
				cnpj:String = "", telefones:ArrayCollection = null,
                endereco:Endereco = null, id:* = null){
			this.id = id;
            this.tipo = tipo;
			this.nomeFantasia = nomeFantasia;
			this.razaoSocial = razaoSocial;
			this.email = email;
			this.cnpj = cnpj;
			this.telefones = telefones ? telefones : new ArrayCollection();
            this.endereco = endereco;
		}
		
		public function toString():String{
			return this.nomeFantasia;
		}
		
		public function clone():Empresa{
			return new Empresa(this.tipo, this.nomeFantasia, this.razaoSocial, this.email, this.cnpj, cloneTelefones(this.telefones),
                    this.endereco ? this.endereco.clone() : null,this.id);
		}

        private function cloneTelefones(telefones:ArrayCollection):ArrayCollection {
            var result:ArrayCollection = new ArrayCollection();
            for each(var telefone:Telefone in telefones){
                result.addItem(telefone.clone())
            }
            return result;
        }

        private function cloneEnderecos(enderecos:ArrayCollection):ArrayCollection {
            var result:ArrayCollection = new ArrayCollection();
            for each(var endereco:Endereco in enderecos){
                result.addItem(endereco.clone())
            }
            return result;
        }

		public function equalsId(outro:Empresa):Boolean{
			if(this.id&&outro){
				return this.id == outro.id;
			}
			return false;
		}

	}
}