package br.com.cotecom.tests.util

import br.com.cotecom.domain.item.Produto
import br.com.cotecom.util.Path.Path
import br.com.cotecom.util.documents.excel.ExcelFile
import br.com.cotecom.util.documents.excel.ExcelImport
import grails.test.GrailsUnitTestCase

public class ImportFileTests extends GrailsUnitTestCase{

    String caminhoPlanilha

    void setUp(){
        super.setUp()
        caminhoPlanilha = new Path().getPathArquivos() + File.separator + "test" + File.separator +
                'lista_produtos_teste.xls'
    }

    void tearDown(){
        super.tearDown()
    }

    void testImportListaProdutosFormatadaCorretamente(){
        ExcelFile excelFile = new ExcelFile(caminhoPlanilha)
        List<Produto> produtos = new ExcelImport(excelFile).importProdutos(0)

        assertEquals 199, produtos.size()
        List<Produto> produtosAProcurar = new ArrayList()
        produtosAProcurar.push(new Produto(barCode: "7891000016688", descricao: "ACHOC.NESCAU PO POWER            400GR",
                categoria: "11", marca: "NESCAU"))
        produtosAProcurar.push(new Produto(barCode: "27378", descricao: "ALIM.INF.NESLTE AMEIXA  120GR",
                categoria: "11", marca: "NESTLE"))
        produtosAProcurar.push(new Produto(barCode: "7896075701071", descricao: "ALICATE P/CUT.MERHEJE CJ.C/4 PCS PROF4X1",
                categoria: "23", marca: "MERHEJE"))
        produtosAProcurar.push(new Produto(barCode: "24596", descricao: "ALCOOL MINALCOOL 92,8 INPM         500ML",
                categoria: "14", marca: "MINALCOOL"))
        Produto produto = new Produto(barCode: "12341", descricao: "PRODUTO NAO EXISTENTE",
                        categoria: "12", marca: "TESTE")


        produtosAProcurar.each {Produto produtoAProcurar ->
            assertNotNull produtos.find {Produto prod ->
                prod.equalsPropriedades(produtoAProcurar)
            }
        }

        assertNull produtos.find { Produto produto1->
            produto1.equalsPropriedades(produto)
        }

    }

    void testImportListaProdutosFormatadaIncorretamente(){
        ExcelFile excelFile = new ExcelFile(caminhoPlanilha)
        List<Produto> produtos = new ExcelImport(excelFile).importProdutos(1)
        assertEquals 0, produtos.size()
    }

}