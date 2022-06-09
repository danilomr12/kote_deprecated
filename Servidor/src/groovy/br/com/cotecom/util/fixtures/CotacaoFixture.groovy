package br.com.cotecom.util.fixtures

import br.com.cotecom.domain.cotacao.Cotacao
import br.com.cotecom.domain.cotacao.CotacaoFactory
import br.com.cotecom.domain.cotacao.ICotacao
import br.com.cotecom.domain.resposta.Resposta
import br.com.cotecom.domain.resposta.RespostaRespondaPedidoPendente
import br.com.cotecom.domain.resposta.RespostaRespondendo
import br.com.cotecom.domain.usuarios.Comprador
import br.com.cotecom.domain.usuarios.Representante
import br.com.cotecom.domain.usuarios.empresa.Endereco
import br.com.cotecom.domain.item.*
import br.com.cotecom.domain.resposta.RespostaNovaCotacao
import br.com.cotecom.domain.usuarios.empresa.Cliente

public class CotacaoFixture {

    static public ICotacao crieCotacao() {
        ICotacao cotacao = CotacaoFactory.crie("cotacao de teste", "mensagem", new Date(2010, 10, 10),
                new Date(2010, 10, 10), "35", Cliente.build(), crieEndereco())

        10.times {int index ->
            cotacao.addToItens(crieItemCotacao(index+1))
        }

        cotacao.salve()
        return cotacao
    }

    static def crieItemCotacao(Integer quantidade) {
        Produto produto = new Produto(descricao: "produto${quantidade}", categoria: "categoria1${quantidade}",
                embalagem: new EmbalagemVenda( tipoEmbalagemDeVenda: new TipoEmbalagem(descricao:"cx"),
                        tipoEmbalagemUnidade: new TipoEmbalagem(descricao:"un"), qtdeDeUnidadesNaEmbalagemDeVenda: quantidade )).save(flush:true)
        ItemCotacao itemCotacao = new ItemCotacao(produto: produto, quantidade: quantidade)
        return itemCotacao
    }

    private static def Endereco crieEndereco() {
        return new Endereco(logradouro: "rua 1", numero: 1, cidade: "goiania", estado: "GO", bairro:'Cidade de Deus')
    }

    static public ICotacao crieCotacao(Comprador comprador) {
        Endereco enderecoEntrega = crieEndereco()
        enderecoEntrega.save(flush:true)
        ICotacao cotacao = CotacaoFactory.crie("cotacao de teste", "mensagem", new Date(2010, 10, 10),
                new Date(2010, 10, 10), "28", comprador.clientes.getAt(0), enderecoEntrega)

        40.times {int index ->
            cotacao.addToItens(crieItemCotacao(index+1))
        }

        cotacao.salve()
        return cotacao
    }

    static Cotacao crieCotacaoAguardandoRespostas(Comprador comprador) {
        Cotacao cotacao = CotacaoFixture.crieCotacao(comprador)
        List<Representante> representantes = UsuarioFixture.crieTresRepresentantes()
        CotacaoFixture.crieItensCotacao(cotacao, 50).each {cotacao.addToItens(it)}
        cotacao.salve()
        if(cotacao.envie(representantes))
            return cotacao
        return null
    }

    static Cotacao crieCotacaoAguardandoRespostas() {
        Cotacao cotacao = CotacaoFixture.crieCotacao(UsuarioFixture.crieCompradorComEmpresa())
        List<Representante> representantes = UsuarioFixture.crieTresRepresentantes()
        CotacaoFixture.crieItensCotacao(cotacao, 50).each {cotacao.addToItens(it)}
        cotacao.salve()
        if(cotacao.envie(representantes))
            return cotacao
        return null
    }

    static def crieItensCotacao(Cotacao cotacao, Integer qtdd) {
        def itens = []
        for( int i = 0; i < qtdd; i++ ) {
            itens << crieItemCotacao(cotacao, i+1)
        }
        return itens
    }

    static def crieItemCotacao(Cotacao cotacao, Integer i) {
        def produto = ProdutoFixture.crieProduto(i)
        return new ItemCotacao(produto:produto, quantidade:i, cotacao:cotacao)
    }

    static void preencheRespostas(Cotacao cotacao) {
        cotacao.respostas.eachWithIndex { Resposta resposta, int indexResposta ->
            if(resposta.estado instanceof RespostaNovaCotacao)
                resposta.aceite()
            if(resposta.estado instanceof RespostaRespondendo){
                resposta.itens.eachWithIndex { ItemResposta itemResposta, int i ->
                    if((i % 5) != 0){ // Deixa alguns itens sem preencher
                        itemResposta.preco = new Preco(embalagem: new Random().nextInt(999))
                        itemResposta.save(flush:true)
                    }
                }
                resposta.envie()
            }
        }
    }

    static Cotacao crieCotacaoRespondida() {
        def cotacao = CotacaoFixture.crieCotacaoAguardandoRespostas()
        CotacaoFixture.preencheRespostas(cotacao)
        return cotacao
    }

    static ICotacao crieCotacaoAnalisada() {
        def cotacao = crieCotacaoRespondida()
        cotacao.analisar()
        cotacao
    }

    static Cotacao crieCotacaoAguardandoRespostasComDuasRespostasConcluidasEUmaNova() {
        def cotacaoAguardandoRespostas = CotacaoFixture.crieCotacaoAguardandoRespostas()
        cotacaoAguardandoRespostas.respostas.eachWithIndex {Resposta resposta, int index ->
            if (index != 2) {
                resposta.aceite()
                resposta.itens.eachWithIndex {ItemResposta itemResposta, int indice ->
                    if (indice.mod(getRandomNumberExcludingZero(3)) == 0 || indice.mod(getRandomNumberExcludingZero(7)) == 0 ||
                            indice.mod(getRandomNumberExcludingZero(37)) == 0) {
                        itemResposta.preco = new Preco(unitario: getRandomNumberExcludingZero(100));
                    }
                }
                resposta.envie()
            }
        }
        return cotacaoAguardandoRespostas
    }

    private static int getRandomNumberExcludingZero(int max) {
        int randomNumber = new Random().nextInt(max)
        if(randomNumber==0)
            return getRandomNumberExcludingZero(max)
        return randomNumber
    }

}

