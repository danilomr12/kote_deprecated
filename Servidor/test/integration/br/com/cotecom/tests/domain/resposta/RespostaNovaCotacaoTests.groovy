package br.com.cotecom.tests.domain.resposta

import br.com.cotecom.domain.resposta.EstadoResposta
import br.com.cotecom.domain.resposta.IResposta
import br.com.cotecom.util.test.RespostaUnitTestCase

class RespostaNovaCotacaoTests extends RespostaUnitTestCase {


    void setUp() {
        super.setUp()
    }

    void tearDown() {
        super.tearDown()
    }

    /**
     * Ap�s a chegada de uma nova cota��o para o representante ele a salva.
     * O estado final da resposta � respondendo.
     */
    void testAceiteNovaResposta() {

        IResposta resposta = cotacao.respostas.asList().get(0)
        resposta.aceite()
        assertEquals resposta.codigoEstado, EstadoResposta.RESPONDENDO
        assertNotNull resposta.dataCriacao
        assertNotNull resposta.dataSalva

    }

    /**
     * A qualquer minuto o comprador pode cancelar uma cota��o enviada. Nesse caso a
     * resposta tamb�m deve ser cancelada
     */
    void testCanceleCotacaoECanceleNovaResposta() {
        cotacao.cancele()
        cotacao.respostas.each {
            assertEquals EstadoResposta.CANCELADA, it.codigoEstado
        }
    }

    /**
     * Ap�s a chegada de uma nova cota��o para o representante ele a recusa.
     * O estado final da resposta � recusada.
     */
    void testRecuseNovaResposta() {
        IResposta resposta = cotacao.respostas.asList().get(0)
        resposta.recuse()
        assertEquals EstadoResposta.RECUSADA, resposta.codigoEstado
    }

    /**
     * Ap�s a chegada de uma nova cota��o para o representante, c comprador pode
     * descartar a resposta daquele representante antes mesmo dele come�ar a responder.
     * O estado final da resposta � perdida.
     */
    void testDescarteNovaResposta() {
        IResposta resposta = cotacao.respostas.asList().get(0)
        resposta.descarte()
        assertEquals EstadoResposta.PERDIDA, resposta.codigoEstado
    }
}