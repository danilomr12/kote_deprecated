package br.com.cotecom.tests.services

import br.com.cotecom.domain.dto.produto.ProdutoDTO
import br.com.cotecom.domain.item.EmbalagemVenda
import br.com.cotecom.domain.item.Produto
import br.com.cotecom.domain.item.TipoEmbalagem
import br.com.cotecom.dtos.assembler.ProdutoAssembler
import br.com.cotecom.services.ProdutoService
import br.com.cotecom.services.remote.RemoteProdutoService
import br.com.cotecom.util.Path.Path
import br.com.cotecom.util.fixtures.ProdutoFixture
import grails.test.GrailsUnitTestCase

public class ProdutoServiceTests extends GrailsUnitTestCase {

    EmbalagemVenda embalagemVenda
    ProdutoService produtoService
    RemoteProdutoService remoteProdutoService

    void setUp(){
        super.setUp()
        embalagemVenda = new EmbalagemVenda(qtdeDeUnidadesNaEmbalagemDeVenda:12, tipoEmbalagemUnidade:
        new TipoEmbalagem(descricao: TipoEmbalagem.PT), tipoEmbalagemDeVenda: new TipoEmbalagem(descricao: TipoEmbalagem.CX))
    }

    void testRemoverListaProduto() {
        List<Produto> produto = ProdutoFixture.crieListaComDezProdutosDiferentesBarCode()

        def retornoSave = produtoService.save(produto)
        assertFalse "Os produtos ${retornoSave} deveriam ter sidos salvos", retornoSave.isEmpty()
        produto.each {Produto produto1->
            assertTrue "O produto: ${produto1} não foi salvo", retornoSave.contains(produto1)
            assertNotNull "O produto: ${produto1} não está no Banco de Dados", Produto.get(produto1.id)
        }

        def retornoDelete = produtoService.remove(produto)
        assertTrue "Os produtos deveriam ter sido removidos ${retornoDelete.toString()}", (retornoDelete instanceof Boolean) && (retornoDelete )
        produto.each {Produto produto1 ->
            assertFalse "A lista nao deveria conter o produto ${produto1}", retornoDelete.contains(produto1)
            assertTrue "Verificando se o Produto no Banco de Dados foi marcado como deletado",
                    Produto.get(produto1.id).dataDelecao != null
        }
    }

    void testAdicionarListaProduto() {

        List<Produto> produtos = ProdutoFixture.crieListaComDezProdutosDiferentesBarCode()

        def respostaSave = produtoService.save(produtos)

        produtos.each {Produto produto ->
            assertTrue "O produto ${produto} deveria ter sido salvo", respostaSave.contains(produto)
        }
    }

    void testRemoveProduto() {
        Produto produto = ProdutoFixture.crieProduto()

        produtoService.save(produto)

        assertEquals produto, Produto.findByBarCode(produto.barCode)
        produtoService.remove(produto)
        assertTrue Produto.findByBarCode(produto.barCode).dataDelecao != null
    }

    void testAdicionarProduto() {

        Produto produto = ProdutoFixture.crieProduto()

        produtoService.save(produto)

        assertEquals produto, Produto.findByBarCode(produto.barCode)
    }

    void testRemoverProdutoInexistente(){
        Produto p4 = ProdutoFixture.crieProduto()
        assertFalse "O produto ${p4} deveria ter sido removido", produtoService.remove(p4)
    }

    void testSearchProdutosSemParametro(){

        List<Produto> produtosToSave = ProdutoFixture.crieListaComDezProdutosDiferentesBarCode()
        produtoService.save(produtosToSave)

        List<ProdutoDTO> produtosFounds = remoteProdutoService.search()
        List<Produto> produtos = new ArrayList()
        produtos = produtosFounds.collect {ProdutoDTO produtoDTO ->
            ProdutoAssembler.crieProduto(produtoDTO)

        }
        produtosToSave.each {Produto produto ->
            assertTrue produtos.any {Produto produtoFound -> produto.equals(produtoFound)}
        }
    }

    void testSearchProdutosComParametro(){

        List<Produto> produtosToSave = ProdutoFixture.crieListaComDezProdutosDiferentesBarCode()
        produtoService.save(produtosToSave)

        List<ProdutoDTO> produtosDTOEncontrados = remoteProdutoService.search(produtosToSave.get(1).barCode)
        List<Produto> produtosEncontrados = new ArrayList()
        produtosDTOEncontrados.each {ProdutoDTO produtoDTO ->
            produtosEncontrados.push(ProdutoAssembler.crieProduto(produtoDTO))
        }

        assertTrue produtosToSave.any {Produto produto -> produtosEncontrados.get(0).equals(produto)}

        Produto produtoARemover = produtosToSave.get(0)
        produtoService.remove(produtoARemover)
        Produto.reindex()

        produtosDTOEncontrados = remoteProdutoService.search(produtoARemover.barCode)
        produtosEncontrados.clear()
        produtosDTOEncontrados.each {ProdutoDTO produtoDTO ->
            produtosEncontrados.push(ProdutoAssembler.crieProduto(produtoDTO))
        }
        assertFalse produtosEncontrados.any {Produto produto -> produtoARemover.equals(produto)}
        assertTrue produtosDTOEncontrados.isEmpty()

        produtosDTOEncontrados = remoteProdutoService.search("a")
        assertTrue produtosDTOEncontrados.size() == 0
        produtosDTOEncontrados = remoteProdutoService.search("[")
        assertTrue produtosDTOEncontrados.size() == 0
    }

    void testImportePlanilhaDeProdutos(){
        String caminhoPlanilha = new Path().getPathArquivos() + File.separator + "test" + File.separator + "lista_produtos_teste.xls"
        produtoService.importePlanilhaProdutos(caminhoPlanilha)

        def criteria = Produto.createCriteria()

        assertNotNull criteria.list{
            eq("descricao", "ALICATE P/CUT.MERHEJE CJ.C/4 PCS PROF4X1")
            eq("barCode", "7896075701071")
            eq("categoria", "23")
            eq("marca", "MERHEJE")
        }

        def criteria2 = Produto.createCriteria()
        assertNotNull criteria2.list{
            eq("descricao", "ALCOOL MINALCOOL 92,8 INPM         500ML")
            eq("barCode", "24596")
            eq("categoria", "14")
            eq("marca", "MINALCOOL")
        }

        def criteria3 = Produto.createCriteria()
        assertNotNull criteria3.list{
            eq("descricao", "ACHOC.NESCAU PO POWER            400GR")
            eq("barCode", "7891000016688")
            eq("categoria", "11")
            eq("marca", "NESCAU")
        }

        def criteria4 = Produto.createCriteria()
        assertNotNull criteria4.list{
            eq("descricao", "ALIM.INF.NESLTE AMEIXA  120GR")
            eq("barCode", "27378")
            eq("categoria", "11")
            eq("marca", "NESTLE")
        }
    }
}