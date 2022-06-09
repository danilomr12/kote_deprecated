package br.com.cotecom.tests.domain.cotacao.analise

import br.com.cotecom.domain.analise.Analise
import br.com.cotecom.domain.cotacao.CotacaoAguardandoRespostas
import br.com.cotecom.domain.cotacao.ICotacao
import br.com.cotecom.domain.usuarios.Representante
import br.com.cotecom.services.CotacaoService
import br.com.cotecom.util.fixtures.CotacaoFixture
import br.com.cotecom.util.fixtures.UsuarioFixture
import grails.test.GrailsUnitTestCase

public class AnaliseTests extends GrailsUnitTestCase {

	Analise analise
	ICotacao cotacaoRespondida
    CotacaoService cotacaoService
    def grailsApplication

	void setUp() {
        cotacaoRespondida = CotacaoFixture.crieCotacaoRespondida()
        def reps = []
        reps << Representante.build(nome:"Danilo")
        reps << Representante.build(nome:"Alberto")
        cotacaoRespondida.envie(reps)
    }

    void testGereAnaliseDeCotacaoRespondida() {
    /*    def cotacaoRespondida = CotacaoFixture.crieCotacaoRespondida()
        Analise analise = new Analise(cotacaoRespondida)
        assertEquals analise.cotacao, cotacaoRespondida
        cotacaoRespondida.itens.each { ItemCotacao itemCotacao ->
            assertNotNull "O item: $itemCotacao nao foi encontrado na analise",
                    analise.itens.find { it.itemCotacaoId == itemCotacao.id }
        }*/
    }
/*

	void testGereAnaliseDeCotacaoAguardandoResposta() {
 		cotacaoService.envieCotacao(cotacaoRespondida, UsuarioFixture.crieRepresentanteSemEmpresa().save(flush:true))
        assertTrue cotacaoRespondida.estado instanceof CotacaoAguardandoRespostas
        def cotacaoAguardandoRespostas = cotacaoRespondida
		Analise analise = cotacaoService.getAnalise(cotacaoAguardandoRespostas.id)
        assertNotNull analise
        assertNull analise.itens[0].itensRespostaFact.last().precoEmbalagem
	}
*/


}







