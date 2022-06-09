package br.com.cotecom.tests.domain.cotacao

import br.com.cotecom.domain.cotacao.Cotacao
import br.com.cotecom.domain.cotacao.CotacaoFactory
import br.com.cotecom.domain.cotacao.EstadoCotacao
import br.com.cotecom.domain.cotacao.ICotacao
import br.com.cotecom.domain.item.EmbalagemVenda
import br.com.cotecom.domain.item.ItemCotacao
import br.com.cotecom.domain.item.Produto
import br.com.cotecom.domain.item.TipoEmbalagem
import br.com.cotecom.domain.resposta.RespostaFactory
import br.com.cotecom.domain.usuarios.Comprador
import br.com.cotecom.domain.usuarios.Representante
import br.com.cotecom.domain.usuarios.empresa.Cliente
import br.com.cotecom.domain.usuarios.empresa.Endereco
import br.com.cotecom.domain.usuarios.empresa.Fornecedor
import br.com.cotecom.services.CotacaoService
import br.com.cotecom.util.fixtures.EmpresaFixture
import grails.test.GrailsUnitTestCase

public class CotacaoTests extends GrailsUnitTestCase {
    Produto produto
    Comprador comprador1
    Endereco endereco
    Cliente empresa1
    CotacaoService cotacaoService

    void setUp(){
        super.setUp()
        empresa1 = EmpresaFixture.crieClienteComEnderecoETelefone()
        produto = new Produto(descricao:"produto1", categoria:"categoria1").save(flush:true)
        comprador1 = new Comprador(nome:"comprador1", password:"nsn32fs8", empresa: new Cliente(nomeFantasia:"empresa1",
                razaoSocial:"empresa1 LTDA").save(flush:true), email:"comprador1@cotecom.com.br").save(flush:true)
        /**
        * O endereço não vai ser salvo previamente já que existe um cascade de cotação para o endereço
         */
        endereco = EmpresaFixture.crieEnderecoLongo()
    }
                
    void tearDown(){
        super.tearDown()
        produto.delete()
        empresa1.delete()
        comprador1.delete()
    }

    void testCotacaoFactory(){
        assertNull CotacaoFactory.crie(null, null, null, null, null, null,null)
    }

    void testAdicioneItem(){
        ICotacao cotacao = CotacaoFactory.crie("cotacao de teste", "mensagem", new Date(2010,10,10),
                new Date(2010,10,10), "30/60", empresa1, endereco)
        ItemCotacao itemCotacao = new ItemCotacao(produto: produto, quantidade: 12)

        cotacao.addToItens(itemCotacao)
        assertEquals cotacao.itens.size(),1
        assertTrue cotacao.itens.any {ItemCotacao item->
            item.equals(itemCotacao)
        }                       

        cotacao.addToItens(new ItemCotacao(produto:new Produto(descricao:"produto2",categoria:"categoria2").save(flush:true), quantidade:10))
        assertEquals cotacao.itens.size(),2
        assertTrue cotacao.salve()
        cotacao.addToItens(new ItemCotacao(produto:new Produto(descricao:"produto3",categoria:"categoria3").save(flush:true), quantidade:12))
        assertNotNull cotacao.save(flush:true)
        ICotacao cotacao1 = Cotacao.get(cotacao.id)
        assertNotNull cotacao1
        assertEquals cotacao1.itens.size(), 3
    }

    void testSalveCotacao(){
        ICotacao cotacao = CotacaoFactory.crie("cotacao de teste", "mensagem", new Date(2010,10,10),
                new Date(2010,10,10), "35", empresa1, endereco)

        assertEquals cotacao.estado, EstadoCotacao.estado.get(EstadoCotacao.RASCUNHO)
        assertEquals EstadoCotacao.estado.get(cotacao.codigoEstadoCotacao), EstadoCotacao.estado.get(EstadoCotacao.RASCUNHO)

        cotacao.salve()

        assertEquals cotacao.estado, EstadoCotacao.estado.get(EstadoCotacao.RASCUNHO)
        assertEquals EstadoCotacao.estado.get(cotacao.codigoEstadoCotacao), EstadoCotacao.estado.get(EstadoCotacao.RASCUNHO)
        assertNotNull cotacao.id

        cotacao.addToItens(new ItemCotacao(produto: produto,quantidade:12))

        assertTrue cotacao.salve()
        assertEquals cotacao.itens.size(), 1
        assertEquals cotacao.estado, EstadoCotacao.estado.get(EstadoCotacao.RASCUNHO)
        assertEquals EstadoCotacao.estado.get(cotacao.codigoEstadoCotacao), EstadoCotacao.estado.get(EstadoCotacao.RASCUNHO)
    }

    void testSalveCotacaoRascunhoSemCabecalho(){
                   
    }

    void testPossuiResposta(){
        Fornecedor empresa = new Fornecedor(email:EmpresaFixture.crieEmailSimples(), nomeFantasia:"Lider Atac.",
                razaoSocial:"Terra Atacado distr. ltda").save(flush:true)

        Representante representante = new Representante(nome:"representante1",password: '12345', email: EmpresaFixture.crieEmailSimples(),
                empresa: empresa).save(flush:true)

        Cliente cliente = new Cliente(email:EmpresaFixture.crieEmailSimples(), nomeFantasia:"supermercador xx",
                razaoSocial:"xx ltda").save(flush:true)

        Comprador comprador = new Comprador(nome:"alberto", email: EmpresaFixture.crieEmailSimples(), empresa: cliente,
                password:'123456').save(flush:true)

        Produto produtoDeTeste = new Produto(descricao:"produto", categoria:"cat1", barCode:"7891242534",
                 embalagem: new EmbalagemVenda(qtdeDeUnidadesNaEmbalagemDeVenda:12, tipoEmbalagemDeVenda:
                 new TipoEmbalagem(descricao:TipoEmbalagem.UN), tipoEmbalagemUnidade:
                 new TipoEmbalagem(descricao: TipoEmbalagem.DP))).save(flush:true)

        ItemCotacao item1 = new ItemCotacao(produto:produtoDeTeste, quantidade:12)

        ICotacao context = CotacaoFactory.crie("titulo1", "mensagem", new Date(), new Date(), "35", empresa,
                new Endereco(logradouro:"rua s", cidade:"asdsa", estado:"go", bairro: "Centro"))
        context.save(flush:true)
        context.addToItens item1
        context.salve()

        context.addResposta(RespostaFactory.crie(representante, context))
        context.salve()
        assertTrue representante.possuiRespostaDaCotacao(context)

        ICotacao cotacao = CotacaoFactory.crie("cotacao sem enderecorespostas", "mensagem", new Date(),
                new Date(), "35", empresa, new Endereco(logradouro:"rua s", cidade:"asdsa", estado:"go", bairro: "Centro"))
        cotacao.salve()

        assertFalse representante.possuiRespostaDaCotacao(cotacao)
    }


}