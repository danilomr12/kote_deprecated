package br.com.cotecom.tests.domain.cotacao

import br.com.cotecom.domain.cotacao.EstadoCotacao
import br.com.cotecom.domain.resposta.IResposta
import br.com.cotecom.util.test.CotacaoUnitTestCase

public class CotacaoAguardandoRespostasTests extends CotacaoUnitTestCase{

    public void testAguardeRespostas(){
        cotacao.envie([representante1, representante2])
        assertEquals EstadoCotacao.AGUARDANDO_RESPOSTAS, cotacao.codigoEstadoCotacao
        List<IResposta> respostas = cotacao.respostas
        respostas.get(0).aceite()
        respostas.get(0).envie()
        assertEquals EstadoCotacao.AGUARDANDO_RESPOSTAS, cotacao.codigoEstadoCotacao
        respostas.get(1).aceite()
        respostas.get(1).envie()
        assertEquals EstadoCotacao.PRONTA_PARA_ANALISE, cotacao.codigoEstadoCotacao
    }
}