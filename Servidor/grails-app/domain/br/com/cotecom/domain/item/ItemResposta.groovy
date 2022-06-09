package br.com.cotecom.domain.item

import br.com.cotecom.domain.resposta.Resposta
import java.math.MathContext
import java.math.RoundingMode
import org.hibernate.FetchMode

public class ItemResposta {

    static belongsTo = [resposta:Resposta, itemCotacao:ItemCotacao]

    Preco preco = new Preco()
    static embedded = ['preco']

    Produto produtoAlternativo
    String observacao

    static mapping = {
        id generator:'identity'
        cache:true
        itemCotacao fetch:'join'
    }

    static constraints = {
        itemCotacao(nullable:false)
        preco(nullable:true)
        produtoAlternativo(nullable:true)
        observacao(blank:true, nullable:true)
    }

    static List<ItemResposta> getItensParaRespostaEager(Long idResposta){
        def itens = ItemResposta.createCriteria().list{
            eq 'resposta.id', idResposta
            fetchMode "itemCotacao", FetchMode.EAGER
        }
        return itens
    }

    void setPreco(Preco pr){
        if(this.preco != pr && pr){
            if(pr.embalagem){
                this.preco.mudePrecoEmbalagem(pr?.embalagem,
                        this.itemCotacao.produto.embalagem.qtdeDeUnidadesNaEmbalagemDeVenda)
            } else if(pr.unitario)  {
                this.preco.mudePrecoUnitario(pr?.unitario,
                        this.itemCotacao.produto.embalagem.qtdeDeUnidadesNaEmbalagemDeVenda)
            }else{
                this.preco = pr
            }
        }
    }

    def getPreco = {->
        return this.preco
    }

    public int hashCode() {
        if (this.id == null)
            return super.hashCode()
        return this.id.hashCode()
    }

    public boolean equals(Object object) {
        if (this.is(object))
            return true
        if ((object == null) || !(object instanceof ItemResposta))
            return false

        ItemResposta itemToCompare = object as ItemResposta
        if(this.id.is(itemToCompare.id))
            return true
        if((this.id == null) || (itemToCompare.id == null))
            return false
        return this.id.equals(object.id)   //Nunca alterar preço unitário ou embalagem diretamente, somente pelos gets e sets do itemResposta

    }


    public String toString(){
        this.id + " - " + this.itemCotacao.quantidade + " - " + this?.itemCotacao?.produto?.embalagem?.toString() + " - " + this?.itemCotacao?.produto?.descricao + " - " + this?.preco?.embalagem
    }

}

public class Preco {

    BigDecimal unitario
    BigDecimal embalagem

    static mapping = {
        unitario column: 'preco_unitatio'
        embalagem column: 'preco_embalagem'
    }

    static constraints = {
        unitario(min:0 as BigDecimal, notEqual:0 as BigDecimal, nullable: true)
        embalagem(min:0 as BigDecimal, notEqual:0 as BigDecimal, nullable: true)
    }

    void setUnitario(def precoUnitario){   // TODO Era double mas tava dando problema
        if(precoUnitario)
            this.unitario = new BigDecimal(precoUnitario as Double, new MathContext(16, RoundingMode.HALF_UP))
    }

    void setEmbalagem(def precoEmbalagem){
        if(precoEmbalagem)
            this.embalagem = new BigDecimal(precoEmbalagem as Double, new MathContext(16, RoundingMode.HALF_UP))
    }

    void mudePrecoEmbalagem(BigDecimal precoEmb, Integer quantidadeEmbalagem){
        if(precoEmb!= null){
            this.embalagem = precoEmb
            if(quantidadeEmbalagem > 0)
                this.unitario = new BigDecimal (precoEmb,
                        new MathContext(16, RoundingMode.HALF_UP)).divide(
                        quantidadeEmbalagem, 5, RoundingMode.HALF_UP)
        }
    }

    void mudePrecoUnitario(BigDecimal precoUnit, Integer quantidadeEmbalagem){
        if(precoUnit != null){
            this.unitario = precoUnit
            if(quantidadeEmbalagem > 0)
                this.embalagem = precoUnit ? precoUnit.multiply(
                        quantidadeEmbalagem) : null
        }
    }
}

