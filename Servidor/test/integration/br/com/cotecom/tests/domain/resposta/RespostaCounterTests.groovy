package br.com.cotecom.tests.domain.resposta

import br.com.cotecom.domain.cotacao.counter.RespostaCounter
import br.com.cotecom.domain.resposta.EstadoResposta
import br.com.cotecom.domain.resposta.IResposta
import br.com.cotecom.util.test.RespostaUnitTestCase

public class RespostaCounterTests extends RespostaUnitTestCase {

    void testMakeRespostaCounter() {
        RespostaCounter respostaCounter = cotacao.makeRespostasCounter()
        assertEquals 2, respostaCounter.faltando
        assertEquals 0, respostaCounter.recebido

        IResposta resposta = cotacao.respostas.asList().get(0)
        resposta.aceite()
        respostaCounter = cotacao.makeRespostasCounter()
        assertEquals 2, respostaCounter.faltando
        assertEquals 0, respostaCounter.recebido
        resposta.envie()
        respostaCounter = cotacao.makeRespostasCounter()
        assertEquals 1, respostaCounter.faltando
        assertEquals 1, respostaCounter.recebido

        cotacao.respostas.each {
            if (it.codigoEstado == EstadoResposta.NOVA_COTACAO){
                it.aceite()
                it.envie()
            }
        }
        respostaCounter = cotacao.makeRespostasCounter()
        assertEquals 0, respostaCounter.faltando
        assertEquals 2, respostaCounter.recebido

    }
}