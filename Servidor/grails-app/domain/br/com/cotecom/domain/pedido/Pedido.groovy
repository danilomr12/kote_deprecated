package br.com.cotecom.domain.pedido

import br.com.cotecom.domain.item.ItemPedido
import br.com.cotecom.domain.resposta.Resposta

class Pedido {

    Date dataCriacao = new Date()
    String pedidoUrlDigest
    Boolean faturado = false
    static belongsTo = [resposta:Resposta]
    static hasMany = [itens: ItemPedido]


    static mapping = {
        id generator:'identity'
        pedidoUrlDigest column: 'pedido_digest'
    }
    static constraints = {
        dataCriacao(nullable: false)
        itens(minSize:1)
        itens column:'item_pedido_idx'
        pedidoUrlDigest(nullable: true)
    }

    def beforeInsert = {
        if (!this.pedidoUrlDigest)
            this.pedidoUrlDigest = getDigest()
        if (!this.dataCriacao)
            this.dataCriacao = new Date()
    }

    private def getDigest() {
        return (this.dataCriacao.time.toString()).encodeAsUrlDigest()
    }

    // TODO Refatorar
    boolean adicioneItem(ItemPedido itemPedido) {
        this.addToItens(itemPedido)
        return this.itens.contains(itemPedido)
    }

    Double valorTotalPedido(){
        Double valor = 0.0;
        itens.each {ItemPedido itemPedido->
            valor += itemPedido.totalPedido()
        }
        return valor
    }

    public String toString(){
        this.id + " - " + this.resposta.representante.nome + " - " + (this.faturado?"Faturado":"Aguardando faturamento")
    }
}
