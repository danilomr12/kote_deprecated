package br.com.cotecom.domain.cotacao

import br.com.cotecom.domain.analise.Analise
import br.com.cotecom.domain.cotacao.counter.AbstractCounter
import br.com.cotecom.domain.pedido.Pedido
import br.com.cotecom.domain.usuarios.Representante

public abstract class EstadoCotacao {

    public static final Integer RASCUNHO = 0
    public static final Integer AGUARDANDO_RESPOSTAS = 1
    public static final Integer PRONTA_PARA_ANALISE = 2
    public static final Integer EM_ANALISE = 3
    public static final Integer PRONTA_PARA_ENVIO_PEDIDOS = 4
    public static final Integer AGUARDANDO_PEDIDOS = 5
    public static final Integer PRONTA_PARA_ANALISE_FALTAS = 6
    public static final Integer CANCELADA = 7
    public static final Integer FINALIZADA = 8
    public static final Integer PROCESSANDO_ENVIO_RESPOSTAS = 9

    public static final Map descricao = [0: "Rascunho", 1: "Aguardando respostas", 2: "Pronta para análise", 3: "Cotação em análise",
            4: "Pronta para envio de pedidos", 5: "Aguardando pedidos", 6: "Pronta para análise de faltas", 7: "Cancelada",
            8: "Finalizada", 9: "Processando envio de respostas"]

    public static final Map estado = [0: new CotacaoRascunho(), 1: new CotacaoAguardandoRespostas(), 2: new CotacaoProntaParaAnalise(),
            3: new CotacaoEmAnalise(), 5: new CotacaoAguardandoPedidos(),
            6: new CotacaoProntaParaAnaliseFaltas(), 7: new CotacaoCancelada(), 8:  new CotacaoFinalizada(), 9: new CotacaoProcessandoEnvioDeRespostas()]

    public void analisar(ICotacao cotacao) {
        throw new UnsupportedOperationException();
    }

    public Analise getAnalise(ICotacao cotacao) {
        throw new UnsupportedOperationException();
    }

    public boolean salve(ICotacao cotacao) {
        throw new UnsupportedOperationException();
    }

    public void gereFalta(ICotacao cotacao) {
        throw new UnsupportedOperationException();
    }

    public boolean cancele(ICotacao cotacao) {
        throw new UnsupportedOperationException();
    }

    public List<Pedido> gerePedidosPrimeiraOrdem(ICotacao cotacao) {
        throw new UnsupportedOperationException();
    }

    public String exporteExcelAnalise(ICotacao cotacao) {
        throw new UnsupportedOperationException();
    }

    public boolean envie(ICotacao cotacao, List<Representante> representantes){
        throw new UnsupportedOperationException();
    }

    public void finalize(ICotacao cotacao) {
        throw new UnsupportedOperationException();
    }

    public void gereCotacaoDeFalta(ICotacao cotacao) {
        throw new UnsupportedOperationException();
    }

    public void aguardeAnalise(ICotacao cotacao) {
        throw new UnsupportedOperationException();
    }

    public AbstractCounter makeRespostaCounter(ICotacao cotacao) {
        throw new UnsupportedOperationException()
    }

    public String getCompraId(ICotacao cotacao) {
        throw new UnsupportedOperationException()
    }

    /**
     * Um estado são diferenciado do outro pelo nome da classe, já que os estados são "stateless" isto é
     * não possuem campos.
     */
    public final boolean equals(Object object){
        if (this.is(object))
            return true
        if (object == null)
            return false
        return getClass().getName().equals(object.getClass().getName())
    }

    public final int hashCode(){
        return getClass().getName().hashCode()
    }

}