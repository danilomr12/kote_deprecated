package br.com.cotecom.util.fixtures

import br.com.cotecom.domain.item.EmbalagemVenda
import br.com.cotecom.domain.item.Produto
import br.com.cotecom.domain.item.TipoEmbalagem

public class ProdutoFixture {

    public static Produto crieProduto () {
        return new Produto(descricao : 'Produto Dummy', categoria : 'Peso Pesado', barCode : 'Codigo Dummy',
                fabricante : 'Fabricante Dummy', marca: "Paraguai", embalagem : new EmbalagemVenda(tipoEmbalagemDeVenda:
                new TipoEmbalagem(descricao: TipoEmbalagem.CX), tipoEmbalagemUnidade: new TipoEmbalagem(descricao: "DU"),
                        qtdeDeUnidadesNaEmbalagemDeVenda: 12), peso: 12.5)
    }

	static def crieProduto(int i) {
		return new Produto( barCode: "${i + 1}12${i}345${i}", descricao: "produto de teste " + i,
        	categoria: "categoria" + i, fabricante: "fabricante" + i, marca: "marca1" + 1,
            embalagem: ProdutoFixture.crieEmbalagemVenda(i)).save(flush:true)
	}

	static def crieEmbalagemVenda(Integer i) {
		return new EmbalagemVenda( tipoEmbalagemDeVenda:new TipoEmbalagem( descricao:"CX"),
								   qtdeDeUnidadesNaEmbalagemDeVenda:i,
 		  						   tipoEmbalagemUnidade:new TipoEmbalagem(descricao:TipoEmbalagem.UN))
	}

    public static List<Produto> crieListaComDezProdutosDiferentesBarCode() {
        List<Produto> produtos = new ArrayList<Produto>()
        def i = 0
        10.times {
            Produto produto = crieProduto()
            produto.barCode = "${System.currentTimeMillis() + i++}"
            produtos.add(produto)
        }
        return produtos
    }

}