package br.com.cotecom.model.utils
{
	public class EstadoCotacao
	{
		public function EstadoCotacao(){}
		
		public static const RASCUNHO:int = 0;
	    public static const AGUARDANDO_RESPOSTAS:int = 1;
	    public static const PRONTA_PARA_ANALISE:int = 2;
	    public static const EM_ANALISE:int = 3;
	    public static const PRONTA_PARA_ENVIO_PEDIDOS:int = 4;
	    public static const AGUARDANDO_PEDIDOS:int = 5;
	    public static const PRONTA_PARA_ANALISE_FALTAS:int = 6;
	    public static const CANCELADA:int = 7;
	    public static const FINALIZADA:int = 8;
	    public static const PROCESSANDO_ENVIO_DE_RESPOSTAS:int = 9;

		public static const descricao:Array = ["Rascunho", "Aguardando respostas", "Pronta para análise", 
			"Cotação em análise", "Pronta para envio de pedidos", "Aguardando pedidos",
			"Pronta para análise de faltas", "Cancelada", "Finalizada", "Processando envio de respostas"];

		public static function getDescricao(estado:int):String{
			return descricao[estado];
		}
	}
}