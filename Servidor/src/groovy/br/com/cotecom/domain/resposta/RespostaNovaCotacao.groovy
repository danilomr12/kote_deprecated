package br.com.cotecom.domain.resposta

import br.com.cotecom.domain.cotacao.CotacaoHandler
import br.com.cotecom.util.documents.excel.ExcelExport
import br.com.analise.service.ICompraService
import org.codehaus.groovy.grails.commons.ApplicationHolder

public class RespostaNovaCotacao extends EstadoResposta{

    def ctx = ApplicationHolder.application.mainContext
    def compraService = (ICompraService)ctx.getBean("compraService")

    public boolean cancele(IResposta resposta) {
        resposta.lida = false
        resposta.mudeEstadoPara(EstadoResposta.CANCELADA)
        compraService.updateRespostaCompra(resposta.id, EstadoResposta.CANCELADA)
        resposta.estado instanceof RespostaCancelada
    }

    public void descarte(IResposta resposta) {
        compraService.updateRespostaCompra(resposta.id, EstadoResposta.PERDIDA)
        resposta.mudeEstadoPara(EstadoResposta.PERDIDA)
    }

    public void recuse(IResposta resposta) {
        resposta.lida = false
        resposta.mudeEstadoPara(EstadoResposta.RECUSADA)
        compraService.updateRespostaCompra(resposta.id, EstadoResposta.RECUSADA)
        cotacaoHandler.dispatch(CotacaoHandler.RECUSA_RESPOSTA, resposta)
    }

    public void aceite(IResposta resposta) {
        resposta.lida = false
        resposta.mudeEstadoPara(EstadoResposta.RESPONDENDO)
        compraService.updateRespostaCompra(resposta.id, EstadoResposta.RESPONDENDO)
    }

    public IResposta salve(IResposta resposta) {
        return resposta.save()
    }


}