package br.com.cotecom.model.utils
{
public class EstadoResposta
{
    public function EstadoResposta(){}

    public static const NOVA_COTACAO:int = 0;
    public static const AGUARDANDO_OUTRAS_RESPOSTAS:int = 1;
    public static const AGUARDANDO_ANALISE:int = 2;
    public static const EM_ANALISE:int = 3;
    public static const RESPONDA_PEDIDO_PENDENTE:int = 4;
    public static const CANCELADA:int = 5;
    public static const AGUARDANDO_RETORNO_DE_PEDIDOS:int = 6;
    public static const PEDIDO_FATURADO:int = 7;
    public static const PERDIDA:int = 8;
    public static const RECUSADA:int = 9;
    public static const ANALISANDO_FALTAS:int = 10;
    public static const RESPONDENDO:int = 11;

    public static const descricaoVisaoRepresentante:Array = [
        "Nova Cotação",
        "Aguardando outras respostas",
        "Aguardando análise",
        "Em análise",
        "Responda pedido pendente",
        "Cancelada",
        "Aguardando resposta de outros pedidos",
        "Pedido faturado",
        "Perdida",
        "Recusada",
        "Analisando Faltas",
        "Respondendo"
    ];


    public static const descricaoVisaoComprador:Array = [
        "Nova",
        "Respondida",
        "Respondida",
        "Respondida",
        "Aguard. faturamento de pedido",
        "Cancelada",
        "Pedido faturado",
        "Pedido faturado",
        "Perdida",
        "Recusada",
        "Pedido faturado",
        "Respondendo"
    ];

    public static function getDescricaoVisaoRepresentante(estado:int):String {
        return descricaoVisaoRepresentante[estado];
    }

    public static function getDescricaoVisaoComprador(estado:int):String {
        return descricaoVisaoComprador[estado]
    }
}
}