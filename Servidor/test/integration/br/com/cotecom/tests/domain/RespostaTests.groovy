package br.com.cotecom.tests.domain

import br.com.cotecom.domain.cotacao.Cotacao
import br.com.cotecom.domain.item.ItemResposta
import br.com.cotecom.domain.resposta.Resposta
import br.com.cotecom.util.fixtures.CotacaoFixture
import br.com.cotecom.util.test.RespostaUnitTestCase

class RespostaTests extends RespostaUnitTestCase {


    void testRetornoCotacao(){
        cotacao.respostas.each { Resposta resposta ->
            assertEquals resposta.cotacao, cotacao
        }
    }

    void testRespostaDeItemResposta(){
        Cotacao cotacao = CotacaoFixture.crieCotacaoRespondida()

        cotacao.respostas.each { Resposta reposta ->
            reposta.itens.each { ItemResposta itemResposta ->
                assertEquals itemResposta.resposta.cotacao, cotacao
            }
        }
    }

}
