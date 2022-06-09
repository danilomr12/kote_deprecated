package br.com.cotecom.domain.item

import br.com.cotecom.domain.analise.ItemRespostaFact
import br.com.cotecom.domain.pedido.Pedido
import java.math.MathContext
import java.math.RoundingMode

class ItemPedido {

    static belongsTo = [pedido: Pedido, itemResposta:ItemResposta]

    int qtdPedida = 0
    BigDecimal precoEmbalagem
    int qtdFaturada = 0

    static mapping = {
        id generator:'identity'
    }

    BigDecimal calculePrecoUnitario(){
        if(this?.itemResposta?.itemCotacao?.produto?.embalagem)
            return new BigDecimal (this.precoEmbalagem,
                    new MathContext(16, RoundingMode.HALF_UP)).divide(
                    this.itemResposta.itemCotacao.produto.embalagem.qtdeDeUnidadesNaEmbalagemDeVenda, 5, RoundingMode.HALF_UP)
        return 0
    }

    Double totalPedido() {
        this.qtdPedida*this.precoEmbalagem
    }

    Double totalFaturado() {
        this.qtdFaturada*this.precoEmbalagem
    }

    public String toString(){
        this.id + " - " + this.qtdPedida + " - " + this.itemResposta.itemCotacao.produto.descricao
    }

}
