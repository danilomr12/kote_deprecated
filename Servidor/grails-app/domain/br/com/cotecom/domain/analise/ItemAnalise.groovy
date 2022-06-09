package br.com.cotecom.domain.analise

import br.com.cotecom.domain.item.ItemCotacao

class ItemAnalise {

    ItemCotacao itemDeCotacao

    // TODO Verificar se esse problema de copiar dados do item de cotacao pode ser resolvido com o fetch 'join'
    // TODO Colocar como transientes
    Boolean naoComprar = false
    String descricao
    int quantidade
    String embalagem

    static belongsTo = [analise:Analise]

    List<ItemRespostaFact> itensRespostaFact = new ArrayList<ItemRespostaFact>()
    static hasMany = [itensRespostaFact:ItemRespostaFact]

    static mapping = {
        id generator:'foreign', params:[property:'itemDeCotacao']
        cache:true
    }
    static constraints = {
        descricao(nullable:false, blank:false)
        embalagem(nullable:false, blank:false)
        quantidade(min:0)
        itensRespostaFact(nullable:true)
        itemDeCotacao(nullable: false)
    }

    def ItemAnalise(){}

    def ItemAnalise(ItemCotacao itemCotacao, Analise analise){
        this.itemDeCotacao = itemCotacao
        this.descricao = itemCotacao.produto.descricao
        this.embalagem = itemCotacao.produto.embalagem.toString()
        this.quantidade = itemCotacao.quantidade
        this.analise = analise
    }

    public void ordeneItemAnalise(){
        itensRespostaFact.sort {ItemRespostaFact first, ItemRespostaFact sec ->
            if (first.precoEmbalagem == null || first.precoEmbalagem == 0)
                return 1
            if (sec.precoEmbalagem == null || sec.precoEmbalagem == 0)
                return -1
            return first.precoEmbalagem <=> sec.precoEmbalagem
        }
    }

    int hashCode() {
        if (this.id == null)
            return super.hashCode()
        return this.id.hashCode()
    }

    boolean equals(Object object) {
        if (this.is(object))
            return true
        if ((object == null) || !(object instanceof ItemAnalise))
            return false

        ItemAnalise analiseItemToCompare = object as ItemAnalise
        if(this.id.is(analiseItemToCompare.id))
            return true
        if((this.id == null) || (analiseItemToCompare.id == null))
            return false
        return this.id.equals(object.id)
    }
}

