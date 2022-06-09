package br.com.cotecom.tests.services.remote

import br.com.cotecom.domain.cotacao.ICotacao
import br.com.cotecom.domain.dto.produto.ProdutoDTO
import br.com.cotecom.domain.item.ItemCotacao
import br.com.cotecom.domain.item.Produto
import br.com.cotecom.dtos.assembler.ProdutoAssembler
import br.com.cotecom.services.remote.RemoteProdutoService
import br.com.cotecom.util.Path.Path
import br.com.cotecom.util.fixtures.CotacaoFixture
import br.com.cotecom.util.fixtures.ProdutoFixture
import grails.test.GrailsUnitTestCase

public class RemoteProdutoServiceTests extends GrailsUnitTestCase {

    RemoteProdutoService remoteProdutoService

    void testAdicionarListaProdutoRemoteFacade() {

        List<Produto> produtos = ProdutoFixture.crieListaComDezProdutosDiferentesBarCode()
        List<ProdutoDTO> produtoDTOs = produtos.collect {Produto produto -> ProdutoAssembler.crieProdutoDTO(produto)}
        List<ProdutoDTO> resultProdutosDTOs0 = remoteProdutoService.saveProdutos(produtoDTOs)
        assertNotNull "O retorno: ${resultProdutosDTOs0} não deveria ser nulo", resultProdutosDTOs0

        assertTrue "A lista retornada: $resultProdutosDTOs0 não deveria ter tamanho zero", resultProdutosDTOs0.size() != 0
        produtoDTOs?.each {ProdutoDTO produto ->
            boolean saved = resultProdutosDTOs0.any { ProdutoDTO produtoFound ->
                compareProdutoDTO(produto, produtoFound)
            }
            assertTrue "Verificando se todos os produtos foram salvos", saved
        }

        resultProdutosDTOs0.eachWithIndex {ProdutoDTO it, Integer index ->
            it.descricao += " produto alterado ${index}"
        }
        List<ProdutoDTO> resultProdutosDTOs1 = remoteProdutoService.saveProdutos(resultProdutosDTOs0)

        resultProdutosDTOs1.eachWithIndex {ProdutoDTO produto, Integer index ->
            assertTrue produto.version == 1
            assertNull Produto.findByBarCodeAndDescricao(produto.barCode,
                    produto.descricao.substring(0, produto.descricao.length() - 19))
        }

        ICotacao cotacao = CotacaoFixture.crieCotacao()

        resultProdutosDTOs1.each {ProdutoDTO produto ->
            cotacao.addToItens(new ItemCotacao(produto: ProdutoAssembler.crieProduto(produto), quantidade: 1))
        }
        cotacao.save(flush:true)

        resultProdutosDTOs1.eachWithIndex {ProdutoDTO it, Integer index ->
            it.descricao = it.descricao.substring(0, it.descricao.length() - 19)
        }

        List<ProdutoDTO> resultProdutosDTOs2 = remoteProdutoService.saveProdutos(resultProdutosDTOs1)

        List<Produto> produtosPersistidos = Produto.list()
        resultProdutosDTOs2.eachWithIndex {ProdutoDTO produto, Integer index ->
            assertTrue produto.version == 0
            def produto1 = produtosPersistidos.find {
                (it.descricao == produto.descricao + " produto alterado ${index}") &&
                        it.barCode == produto.barCode
            }
            assertNotNull produto1.dataDelecao
            def produto2 = produtosPersistidos.find {
                it.barCode == produto.barCode && it.descricao == produto.descricao
            }
            assertNotNull produto2.dataModificacao
        }
    }

    void testRemoverListaProdutos() {
        List<Produto> produtos = ProdutoFixture.crieListaComDezProdutosDiferentesBarCode()
        List<ProdutoDTO> produtoDTOs = produtos.collect {Produto produto -> ProdutoAssembler.crieProdutoDTO(produto)}
        def produtosSaved = remoteProdutoService.saveProdutos(produtoDTOs)
        produtosSaved.each { ProdutoDTO produtoDTO ->
            assertNotNull Produto.get(produtoDTO.id)
        }
        remoteProdutoService.remove(produtosSaved)
        produtoDTOs.each { ProdutoDTO produtoDTO ->
            assertNull Produto.get(produtoDTO.id)
        }
    }

    void testImportPlanilhaProdutos() {
        String caminhoPlanilha = new Path().getPathArquivosDeTeste() + File.separator + 'lista_produtos_teste.xls'

        File file = new File(caminhoPlanilha);
        byte[] bytes = file.readBytes()
        remoteProdutoService.importePlanilhaProdutos(bytes)

        Produto produtoNoBanco = Produto.findByBarCode("7891000016688")
        assertNotNull produtoNoBanco
        assertTrue produtoNoBanco.descricao == "ACHOC.NESCAU PO POWER            400GR"
        assertTrue produtoNoBanco.categoria == "11"
        assertTrue produtoNoBanco.marca == "NESCAU"
    }

    private boolean compareProdutoDTO(ProdutoDTO produto1, ProdutoDTO produto2) {
        return produto1.barCode == produto2.barCode &&
                produto1.categoria == produto2.categoria &&
                produto1.descricao == produto2.descricao &&
                produto1.embalagem == produto2.embalagem &&
                produto1.fabricante == produto2.fabricante &&
                produto1.marca == produto2.marca &&
                produto1.peso == produto2.peso
    }
}