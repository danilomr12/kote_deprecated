package br.com.cotecom.tests.fixtures

import br.com.cotecom.domain.cotacao.CotacaoAguardandoRespostas
import br.com.cotecom.domain.cotacao.CotacaoEmAnalise
import br.com.cotecom.domain.usuarios.Comprador
import br.com.cotecom.util.fixtures.CotacaoFixture
import grails.test.GrailsUnitTestCase

public class CotacaoFixtureTests extends GrailsUnitTestCase {

	void testGetCotacao() {
		def cotacao = CotacaoFixture.crieCotacao(Comprador.build())
		assertNotNull "ICotacao n foi instanciada", cotacao
	}

	void testGetCotacaoAguardandoRespostas() {
		def cotacaoAguardandoRespostas = CotacaoFixture.crieCotacaoAguardandoRespostas(Comprador.build())
		assertNotNull "Falha ao criar instancia", cotacaoAguardandoRespostas
		assertTrue "${cotacaoAguardandoRespostas.estado.class} != ${CotacaoAguardandoRespostas.class}",
			cotacaoAguardandoRespostas.estado instanceof CotacaoAguardandoRespostas
	}

	void testGet50ItensCotacao() {
        def comprador = Comprador.build()
		def itens = CotacaoFixture.crieItensCotacao(
			CotacaoFixture.crieCotacao(comprador), 50)
		assertNotNull "Itens nao deveria ser null", itens
		assertLength 50, itens as Object[]
		itens.each { def item ->
			assertNotNull "Itens sem produtos", item.produto
			assertNotNull "Itens sem qtdPedida", item.quantidade
		}
	}

    void testCrieCotacaoAnalisada(){
        def cotacaoAnalisada = CotacaoFixture.crieCotacaoAnalisada()
        assertNotNull cotacaoAnalisada
        assertTrue cotacaoAnalisada.estado instanceof CotacaoEmAnalise
    }

}