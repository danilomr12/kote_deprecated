package br.com.cotecom.domain.cotacao

import br.com.cotecom.domain.analise.Analise
import br.com.cotecom.domain.cotacao.counter.AbstractCounter
import br.com.cotecom.domain.cotacao.counter.PedidoCounter
import br.com.cotecom.domain.item.ItemCotacao
import br.com.cotecom.domain.pedido.Pedido
import br.com.cotecom.domain.resposta.EstadoResposta
import br.com.cotecom.domain.resposta.IResposta
import br.com.cotecom.domain.resposta.Resposta
import br.com.cotecom.domain.usuarios.Representante
import br.com.cotecom.domain.usuarios.empresa.Endereco
import br.com.cotecom.domain.usuarios.empresa.Cliente

class Cotacao implements ICotacao, ITransicaoEstadoCotacao {

    String titulo
    String mensagem

    Date dataCriacao
    Date dataEntrega
    Date dataValidade
    Date dataSalva
    String prazoPagamento
    Cliente empresa
    Endereco enderecoEntrega
    String compraId

    Integer codigoEstadoCotacao
    EstadoCotacao estado
    static transients = ['estado', 'analise']

    static hasMany = [itens: ItemCotacao, respostas: Resposta]

    static mapping = {
        id generator: 'identity'
        cache: true
        enderecoEntrega cascade: 'all-delete-orphan'
        respostas cascade: 'delete'
    }

    static constraints = {
        titulo nullable: true
        dataCriacao nullable: false
        dataEntrega nullable: true
        dataValidade nullable: true
        dataSalva nullable: false
        prazoPagamento nullable: true
        empresa nullable: false
        enderecoEntrega nullable: true
        codigoEstadoCotacao nullable: false
        mensagem(nullable: true, blank: true)
        compraId(nullable: true, blank: true)
    }

    def beforeUpdate = {
        if (!this.dataCriacao)
            this.dataCriacao = new Date()
        this.dataSalva = new Date()
    }

    def beforeInsert = {
        if (!this.dataCriacao)
            this.dataCriacao = new Date()
        this.dataSalva = new Date()
    }

    void setCodigoEstadoCotacao(Integer codigoEstado) {
        this.codigoEstadoCotacao = codigoEstado
        this.estado = EstadoCotacao.estado.get(codigoEstado)
    }

    public ItemCotacao addItemCotacao(ItemCotacao itemCotacao) {
        if(itemCotacao.validate())
            this.addToItens(itemCotacao)
        if (this.itens.contains(itemCotacao))
            return itemCotacao
        return null
    }

    public IResposta addResposta(IResposta resposta) {
        if(resposta.validate())
            this.addToRespostas(resposta)
        if (this.respostas.contains(resposta))
            return resposta
        return null
    }

    public AbstractCounter makeRespostasCounter() {
        return estado.makeRespostaCounter(this)
    }

    public boolean salve() {
        return estado.salve(this)
    }

    public void analisar() {
        estado.analisar(this)
    }

    public void aguardeAnalise() {
        estado.aguardeAnalise(this)
    }

    public void finalize() {
        estado.finalize(this)
    }

    public boolean cancele() {
        return estado.cancele(this)
    }

    public void gereFalta() {
        estado.gereFalta(this)
    }

    public AbstractCounter buildPedidosCounter() {
        return new PedidoCounter(this)
    }

    public void gereCotacaoDeFalta() {
        estado.gereCotacaoDeFalta(this)
    }

    public boolean envie(List<Representante> representantes) {
        return estado.envie(this, representantes)
    }

    public Boolean isRascunho() {
        return estado.equals(EstadoCotacao.RASCUNHO)
    }

    public Boolean isAguardandoRespostas() {
        return estado.equals(EstadoCotacao.AGUARDANDO_RESPOSTAS)
    }

    public Boolean isProntaParaAnalise() {
        return estado.equals(EstadoCotacao.PRONTA_PARA_ANALISE)
    }

    public Boolean isEmAnalise() {
        return estado.equals(EstadoCotacao.EM_ANALISE)
    }

    public Boolean isAguardandoPedidos() {
        return estado.equals(EstadoCotacao.AGUARDANDO_PEDIDOS)
    }

    public Boolean isProntaParaAnaliseFaltas() {
        return estado.equals(EstadoCotacao.PRONTA_PARA_ANALISE_FALTAS)
    }

    public Boolean isFinalizada() {
        return estado.equals(EstadoCotacao.FINALIZADA)
    }

    public Boolean isCancelada() {
        return estado.equals(EstadoCotacao.CANCELADA)
    }

    public void mudeEstadoPara(Integer codigoEstadoCotacao) {
        this.setCodigoEstadoCotacao(codigoEstadoCotacao)
    }

    public def canceleResposta(Resposta resposta) {
        resposta.mudeEstadoPara(EstadoResposta.CANCELADA)
    }

    public List<Pedido> gerePedidosPrimeiraOrdem() {
        estado.gerePedidosPrimeiraOrdem(this)
    }


    public String exporteExcelAnalise() {
        estado.exporteExcelAnalise(this)
    }

    public int hashCode() {
        if (this.id == null)
            return super.hashCode()
        return this.id.hashCode()
    }

    public boolean equals(Object object) {
        if (this.is(object))
            return true
        if ((object == null) || !(object instanceof ICotacao))
            return false

        ICotacao cotacaoToCompare = object as ICotacao
        if (this.id.is(cotacaoToCompare.id))
            return true
        if ((this.id == null) || (cotacaoToCompare.id == null))
            return false
        return this.id.equals(object.id)
    }

    public String toString(){
        return this.id + "- " + this.titulo + " - " + EstadoCotacao.descricao[this.codigoEstadoCotacao]
    }
}