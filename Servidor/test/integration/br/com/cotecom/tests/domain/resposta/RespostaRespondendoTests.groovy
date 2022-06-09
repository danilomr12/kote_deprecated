package br.com.cotecom.tests.domain.resposta

import br.com.cotecom.domain.cotacao.EstadoCotacao
import br.com.cotecom.domain.resposta.EstadoResposta
import br.com.cotecom.domain.resposta.IResposta
import br.com.cotecom.domain.resposta.Resposta
import br.com.cotecom.util.test.RespostaUnitTestCase

class RespostaRespondendoTests extends RespostaUnitTestCase {

    Resposta resposta

    void setUp() {
        super.setUp()

        resposta = cotacao.respostas.asList().get(0)
        resposta.aceite()

    }

    /**
     * Ap�s a chegada de uma nova cota��o para o representante ele a salva.
     * O estado final da resposta � respondendo.
     */
    void testSalveRespostaRespondendo() {

        IResposta resposta = cotacao.respostas.asList().get(0)
        resposta.salve()
        assertEquals resposta.codigoEstado, EstadoResposta.RESPONDENDO
        assertNotNull resposta.dataCriacao
        assertNotNull resposta.dataSalva

    }

    /**
     * A qualquer minuto o comprador pode cancelar uma cota��o enviada. Nesse caso a
     * resposta tamb�m deve ser cancelada
     */
    void testCanceleCotacaoECanceleRespostaRespondendo() {
        cotacao.cancele()

        cotacao.respostas.each {
            assertEquals EstadoResposta.CANCELADA, it.codigoEstado
        }
    }

    /**
     * Ap�s a chegada de uma nova cota��o para o representante ele a recusa.
     * O estado final da resposta � recusada.
     */
    void testRecuseRespostaRespondendo() {
        IResposta resposta = cotacao.respostas.asList().get(0)
        resposta.recuse()
        assertEquals EstadoResposta.RECUSADA, resposta.codigoEstado
    }

    void testEnvieRespostaRespondendo(){
        cotacao.respostas.each { IResposta resposta ->
            if(resposta.codigoEstado == EstadoResposta.NOVA_COTACAO)
                resposta.aceite()
        }
        cotacao.respostas.asList().get(0).envie()
        assertTrue cotacao.respostas.any { IResposta resposta ->
            resposta.codigoEstado == EstadoResposta.PEDIDO_FATURADO
        }
        cotacao.respostas.each { IResposta resposta ->
            if(resposta.codigoEstado == EstadoResposta.RESPONDENDO)
                resposta.envie()
        }
        assertEquals EstadoCotacao.PRONTA_PARA_ANALISE, cotacao.codigoEstadoCotacao
        cotacao.respostas.each { IResposta resposta ->
            assertEquals EstadoResposta.PEDIDO_FATURADO, resposta.codigoEstado
        }
    }
}