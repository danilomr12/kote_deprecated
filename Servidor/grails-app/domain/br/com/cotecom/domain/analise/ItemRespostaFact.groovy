package br.com.cotecom.domain.analise

import br.com.cotecom.domain.item.ItemResposta
import br.com.cotecom.domain.resposta.Resposta
import br.com.cotecom.domain.usuarios.Representante
import java.math.MathContext
import java.math.RoundingMode
import org.apache.log4j.Logger

class ItemRespostaFact implements Comparable<ItemRespostaFact> {

    private static final log = Logger.getLogger(ItemRespostaFact.class)

    BigDecimal precoEmbalagem
    String observacao
    String nomeRepresentante

    Representante representante
    Resposta resposta
    ItemResposta itemResposta

    static belongsTo = [itemAnalise:ItemAnalise]

    static mapping = {
        id generator:'foreign', params:[property:'itemResposta']
        cache:true
    }

    static constraints = {
        precoEmbalagem(nullable:true)
        nomeRepresentante(nullable:false)
        observacao(nullable:true, blank:true)
        representante nullable:false
        itemAnalise nullable:false
        itemResposta nullable:false
        resposta nullable:false
    }

    BigDecimal calculePrecoUnitario(){
        if(this?.itemAnalise?.embalagem)
            return new BigDecimal (this.precoEmbalagem,
                    new MathContext(16, RoundingMode.HALF_UP)).divide(
                    this.itemAnalise.calculeQuantidadeDaEmbalagem(), 5, RoundingMode.HALF_UP)
        return 0
    }

    int compareTo(ItemRespostaFact o) {
        precoEmbalagem.compareTo(o.precoEmbalagem);
    }

    void atualize(ItemResposta itemResposta) {
        this.precoEmbalagem = itemResposta.preco.embalagem
        this.observacao = itemResposta.observacao
        this.save(flush:true)
    }
}
