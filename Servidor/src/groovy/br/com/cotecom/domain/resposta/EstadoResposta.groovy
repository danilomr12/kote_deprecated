package br.com.cotecom.domain.resposta

import br.com.cotecom.domain.cotacao.CotacaoHandler

abstract class EstadoResposta {

    public static final Integer NOVA_COTACAO = 0
    public static final Integer AGUARDANDO_OUTRAS_RESPOSTAS = 1
    public static final Integer AGUARDANDO_ANALISE = 2
    public static final Integer EM_ANALISE = 3
    public static final Integer RESPONDA_PEDIDO_PENDENTE = 4
    public static final Integer CANCELADA = 5
    public static final Integer AGUARDANDO_RETORNO_DE_PEDIDOS = 6
    public static final Integer PEDIDO_FATURADO = 7
    public static final Integer PERDIDA = 8
    public static final Integer RECUSADA = 9
    public static final Integer ANALISANDO_FALTAS = 10
    public static final Integer RESPONDENDO = 11


    public static final Map descricao = [0: "Nova Cotação", 1: "Aguardando outras respostas", 2: "Pronta para análise",
            3: "Em análise", 4: "Responda pedido pendente", 5:"Cancelada", 6: "Aguardando retorno de pedidos",
            7:"Pedido faturado", 8:"Perdida", 9:"Recusada",10: "Analisando faltas", 11:"Respondendo"]

    public static final Map estado = [0: new RespostaNovaCotacao(), 1: new RespostaAguardandoOutrasRespostas(),
            2: new RespostaProntaParaAnalise(), 3: new RespostaEmAnalise(), 4: new RespostaRespondaPedidoPendente(),
            5: new RespostaCancelada(), 6: new RespostaAguardandoRetornoPedidos(), 7: new RespostaPedidoFaturado(),
            8: new RespostaPerdida(), 9: new RespostaRecusada(), 10: new RespostaAnalisandoFaltas(),
            11: new RespostaRespondendo()]

    def cotacaoHandler = CotacaoHandler.getInstance()

    public void descarte(IResposta resposta) {
        throw new UnsupportedOperationException()
    }

    public boolean cancele(IResposta resposta) {
        throw new UnsupportedOperationException()
    }

    public void recuse(IResposta resposta) {
        throw new UnsupportedOperationException()
    }

    public IResposta salve(IResposta resposta) {
        throw new UnsupportedOperationException()
    }

    public void aceite(IResposta resposta) {
        throw new UnsupportedOperationException()
    }

    public boolean envie(IResposta resposta) {
        throw new UnsupportedOperationException()
    }

    public IResposta ressuscite(IResposta resposta) {
        throw new UnsupportedOperationException()
    }

    public String criePlanilhaParaPreenchimentoOffLine(IResposta resposta) {
        throw new UnsupportedOperationException()
    }

    public def importePlanilhaResposta(Resposta resposta, String pathPlanilha) {
        throw new UnsupportedOperationException()
    }

    public Boolean faturePedido(IResposta resposta) {
        throw new UnsupportedOperationException()
    }
}