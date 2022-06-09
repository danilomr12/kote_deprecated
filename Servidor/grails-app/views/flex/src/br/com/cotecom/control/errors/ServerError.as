package br.com.cotecom.control.errors
{
	[RemoteClass(alias="br.com.cotecom.domain.error.ServerError")]
	public class ServerError
	{
		public var id:*;
		public var acao:String;
		public var mensagem:String;
		public var causa:String;
		
		public function ServerError(id: * = null, acao:String = "", mensagem:String = "", causa:String = "")
		{
			this.id = id;
			this.acao = acao;
			this.mensagem = mensagem;
			this.causa = causa;
		}

        public function toString():String{
            return "Acao: " + acao + "\nCausa: " + causa + "\nMensagem: " + mensagem;
        }
	}
}