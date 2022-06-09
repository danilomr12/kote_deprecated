package br.com.cotecom.model.domain.dtos.usuarios
{
import br.com.cotecom.model.utils.ArrayCollections;

import mx.collections.ArrayCollection;

[RemoteClass(alias="br.com.cotecom.domain.dto.usuario.UsuarioDTO")]
	[Bindable]
	public class Usuario
	{
		public var id:*;
		public var nome:String;
		public var email:String;
        public var password:String;
		public var empresa:Empresa;
		public var telefones:ArrayCollection;
        public var tipo:int;
        public var supervisor:Usuario;
        public var isSwitched:Boolean;

		public function Usuario(tipo:int = 0, nome:String = "", email:String = "", telefones:ArrayCollection = null,empresa:Empresa = null, id:* = null){
			this.id = id;
			this.nome = nome;
			this.email = email;
			this.empresa = empresa;
			this.telefones = telefones;
            this.tipo = tipo;
		}
		
		public function equalsId(outroComprador:Usuario):Boolean {
			return this.id == outroComprador.id;
		}

        public function clone():Usuario{
            var usuario:Usuario = new Usuario(this.tipo, this.nome, this.email, ArrayCollections.cloneAll(this.telefones),
                    this.empresa ? this.empresa.clone() : null,
                    this.id);
            usuario.supervisor = this.supervisor ? this.supervisor.clone() : null
            return usuario;
        }

	}
}