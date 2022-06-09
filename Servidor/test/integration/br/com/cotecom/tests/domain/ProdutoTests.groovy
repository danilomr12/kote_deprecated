package br.com.cotecom.tests.domain

import br.com.cotecom.domain.item.EmbalagemVenda
import br.com.cotecom.domain.item.Produto
import br.com.cotecom.domain.item.TipoEmbalagem
import br.com.cotecom.services.ProdutoService
import grails.test.GrailsUnitTestCase

public class ProdutoTests extends GrailsUnitTestCase {

    boolean transactional = true
    EmbalagemVenda embalagemVenda
    ProdutoService produtoService

    void setUp() {
        super.setUp()
        embalagemVenda = new EmbalagemVenda(qtdeDeUnidadesNaEmbalagemDeVenda:12, tipoEmbalagemUnidade:
        new TipoEmbalagem(descricao: TipoEmbalagem.PT), tipoEmbalagemDeVenda: new TipoEmbalagem(descricao: TipoEmbalagem.CX))
    }

    void tearDown(){
        super.tearDown()
    }

    void testEmbalagemToStringEStringToEmbalagem(){
        EmbalagemVenda emb = EmbalagemVenda.setEmbalagem("cx/0002/un")
        assertNotNull emb
        assertTrue "o tipo de embalagem deveria ser 'un' mas eh $emb.tipoEmbalagemUnidade",
                emb.tipoEmbalagemUnidade.descricao.equalsIgnoreCase("UN")
        assertTrue "o tipo de embalagem deveria ser 'cx' mas eh $emb.tipoEmbalagemDeVenda",
                emb.tipoEmbalagemDeVenda.descricao.equalsIgnoreCase("Cx")
        assertTrue "o tipo de embalagem deveria ser '2' mas eh $emb.qtdeDeUnidadesNaEmbalagemDeVenda",
                emb.qtdeDeUnidadesNaEmbalagemDeVenda == 2
        EmbalagemVenda emb2 = EmbalagemVenda.setEmbalagem("Fd/1232/sC")
        assertNotNull emb2
        assertTrue "o tipo de embalagem deveria ser 'sC' mas eh $emb2.tipoEmbalagemUnidade",
                emb2.tipoEmbalagemUnidade.descricao.equalsIgnoreCase("sC")
        assertTrue "o tipo de embalagem deveria ser 'fd' mas eh $emb2.tipoEmbalagemDeVenda",
                emb2.tipoEmbalagemDeVenda.descricao.equalsIgnoreCase("fd")
        assertTrue "o tipo de embalagem deveria ser '1232' mas eh $emb2.qtdeDeUnidadesNaEmbalagemDeVenda",
                emb2.qtdeDeUnidadesNaEmbalagemDeVenda == 1232
    }

    void testStringToEmbalagemComTipoEmbalagemSemPadrao(){
        EmbalagemVenda emb = EmbalagemVenda.setEmbalagem("pct/00332/pt")
        assertNull emb
        EmbalagemVenda emb2 = EmbalagemVenda.setEmbalagem("1j/27DhG/18")
        assertNull emb2
        EmbalagemVenda emb3 = EmbalagemVenda.setEmbalagem("das/1234/SF")
        assertNull emb3
    }

    void testEmbalagemToString(){
        String toString = embalagemVenda.toString()
        assertTrue toString.equalsIgnoreCase("cx/0012/pt")

        embalagemVenda.setTipoEmbalagemDeVenda(new TipoEmbalagem(descricao:TipoEmbalagem.FD))
        embalagemVenda.setQtdeDeUnidadesNaEmbalagemDeVenda 144

        assertTrue embalagemVenda.toString().equalsIgnoreCase("FD/0144/pt")
    }

    void testSearchProdutosComSearchable(){
        List list = new ArrayList()
        list.push(new Produto(barCode: "7896075701071", descricao: "ALICATE P/CUT.MERHEJE CJ.C/4 PCS PROF4X1",
                categoria: "23", marca: "MERHEJE"))
        list.push(new Produto(barCode: "24596", descricao: "ALCOOL MINALCOOL 92,8 INPM         500ML",
                categoria: "14", marca: "MINALCOOL"))
        list.push(new Produto(barCode: "7891000016688", descricao: "ACHOC.NESCAU PO POWER            400GR",
                categoria: "11", marca: "NESCAU"))
        list.push(new Produto(barCode: "27378", descricao: "ALIM.INF.NESLTE AMEIXA  120GR",
                categoria: "11", marca: "NESTLE"))
        produtoService.save(list)
        list.each{Produto produto->
            Produto.reindex(produto)
        }
        
        def searchableResults = Produto.search("ALICATE")
        assertEquals searchableResults.results.size(),1
        searchableResults = Produto.search("alicate")
        assertEquals searchableResults.results.size(),1
        searchableResults = Produto.search("alicATE merHEJE")
        assertEquals searchableResults.results.size(),1
        searchableResults = Produto.search("minalcool 92,8")
        assertEquals searchableResults.results.size(),1
        searchableResults = Produto.search("alim*")
        assertEquals searchableResults.results.size(),1
        searchableResults = Produto.search("NESCAU power")
        assertEquals searchableResults.results.size(),1
    }
}