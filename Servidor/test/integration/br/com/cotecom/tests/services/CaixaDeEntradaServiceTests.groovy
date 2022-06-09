package br.com.cotecom.tests.services

import br.com.cotecom.domain.cotacao.ICotacao
import br.com.cotecom.domain.dto.inbox.ItemInboxDTO
import br.com.cotecom.domain.item.ItemCotacao
import br.com.cotecom.domain.item.Produto
import br.com.cotecom.domain.usuarios.Comprador
import br.com.cotecom.domain.usuarios.Representante
import br.com.cotecom.domain.usuarios.Usuario
import br.com.cotecom.services.CaixaDeEntradaService
import br.com.cotecom.util.fixtures.CotacaoFixture
import br.com.cotecom.util.fixtures.ProdutoFixture
import br.com.cotecom.util.fixtures.UsuarioFixture
import grails.test.GrailsUnitTestCase

public class CaixaDeEntradaServiceTests extends GrailsUnitTestCase {

    CaixaDeEntradaService caixaDeEntradaService


    void testLoadCaixaDeEntrada() {
        Comprador comprador = Comprador.build()
        ICotacao cotacao = CotacaoFixture.crieCotacao(comprador)
        Produto produtoDeTeste = ProdutoFixture.crieProduto().save(flush:true)
        cotacao.addToItens(new ItemCotacao(produto: produtoDeTeste, quantidade: 12))
        cotacao.addToItens(new ItemCotacao(produto: produtoDeTeste, quantidade: 23))

        cotacao.salve()

        List<Representante> representantes = UsuarioFixture.crieTresRepresentantes()
        representantes.each { it.save(flush:true) }

        assertTrue cotacao.envie(representantes)

        ArrayList<ItemInboxDTO> itens = caixaDeEntradaService.loadCaixaDeEntrada(comprador)

        assertEquals itens.size(), 1
        assertEquals itens.get(0).nome, cotacao.titulo
    }
}