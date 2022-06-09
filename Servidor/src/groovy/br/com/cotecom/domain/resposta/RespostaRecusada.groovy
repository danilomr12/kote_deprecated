package br.com.cotecom.domain.resposta

import org.codehaus.groovy.grails.commons.ApplicationHolder
import br.com.analise.service.ICompraService
import br.com.cotecom.domain.cotacao.EstadoCotacao
import br.com.cotecom.domain.cotacao.Cotacao
import br.com.analise.domain.Compra

public class RespostaRecusada extends EstadoResposta{

    def ctx = ApplicationHolder.application.mainContext
    def compraService = (ICompraService)ctx.getBean("compraService")

    public IResposta ressuscite(IResposta resposta){
        Compra compra = compraService.getCompraByCotacaoId(resposta.cotacao.id)
        if(compra.estado == EstadoCotacao.EM_ANALISE || compra.estado == EstadoCotacao.AGUARDANDO_RESPOSTAS ||
                compra.estado == EstadoCotacao.PRONTA_PARA_ANALISE){
            Cotacao cotacao = Cotacao.get(resposta.cotacao.id)
            cotacao.mudeEstadoPara(EstadoCotacao.AGUARDANDO_RESPOSTAS)
            compraService.updateEstadoCompraByCotacaoId(resposta.cotacao.id, EstadoCotacao.AGUARDANDO_RESPOSTAS)
            resposta.mudeEstadoPara(EstadoResposta.RESPONDENDO)
            compraService.updateRespostaCompra(resposta.id, EstadoResposta.RESPONDENDO)
            return resposta
        }
        return null
    }

    public IResposta salve(IResposta resposta) {
        return resposta.save()
    }
}