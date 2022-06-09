package br.com.cotecom.domain.cotacao

import br.com.cotecom.domain.cotacao.counter.AbstractCounter
import br.com.cotecom.domain.item.ItemCotacao
import br.com.cotecom.domain.resposta.IResposta
import br.com.cotecom.domain.resposta.Resposta
import br.com.cotecom.domain.usuarios.Representante

public interface ICotacao {

    IResposta addResposta(IResposta resposta)
    ItemCotacao addItemCotacao(ItemCotacao itemCotacao)

    AbstractCounter buildPedidosCounter()
    AbstractCounter makeRespostasCounter()

    void gereCotacaoDeFalta()
    void gereFalta()
    def gerePedidosPrimeiraOrdem()
    String getCompraId()
    String exporteExcelAnalise()


    boolean envie(List<Representante> representantes)

    boolean salve()
    boolean cancele()
    void finalize()
    void aguardeAnalise()
    void analisar()
    def canceleResposta(Resposta resposta)

    void mudeEstadoPara(Integer codidoEstadoCotacao)
    Boolean isAguardandoPedidos()
    Boolean isAguardandoRespostas()
    Boolean isCancelada()
    Boolean isEmAnalise()
    Boolean isFinalizada()
    Boolean isProntaParaAnalise()
    Boolean isProntaParaAnaliseFaltas()
	Boolean isRascunho()

}