package br.com.cotecom.domain.analise

import br.com.cotecom.domain.cotacao.Cotacao

class Analise {

    // TODO Implementar campos transientes com resumo da análise
    static belongsTo = [cotacao:Cotacao]

    static hasMany = [itens:ItemAnalise]

    static mapping = {
        id generator:'foreign', params:[property:'cotacao']
        cache:true
        cotacao cascade:'none'
    }
    static constraints = {
        cotacao(nullable:false)
        itens nullable: true
    }

    public def ordeneItensRespostaFact(){
        this.itens.each {
            it.ordeneItemAnalise()
        }
    }

    public void addItemAnalise(ItemAnalise itemAnalise){
        this.addToItens(itemAnalise)
    }

    public int hashCode() {
        if (this.id == null)
            return super.hashCode()
        return this.id.hashCode()
    }

    public boolean equals(Object object) {
        if (this.is(object))
            return true
        if ((object == null) || !(object instanceof Analise))
            return false

        Analise analiseToCompare = object as Analise
        if(this.id.is(analiseToCompare.id))
            return true
        if((this.id == null) || (analiseToCompare.id == null))
            return false
        return this.id.equals(object.id)
    }
}
