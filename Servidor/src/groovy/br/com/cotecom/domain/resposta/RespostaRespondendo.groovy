package br.com.cotecom.domain.resposta

import br.com.cotecom.domain.cotacao.CotacaoHandler
import br.com.cotecom.util.documents.excel.ExcelExport
import br.com.cotecom.util.documents.excel.ExcelFile
import br.com.cotecom.util.documents.excel.ExcelImport
import org.codehaus.groovy.grails.commons.ApplicationHolder
import br.com.analise.service.ICompraService

/**Sem o "import import br.com.cotecom.domain.cotacao.CotacaoHandler"
 * o atributo cotacaoHandler não funciona mesmo estando na superclasse EstadoResposta
 * */

public class RespostaRespondendo extends EstadoResposta{

    def ctx = ApplicationHolder.application.mainContext
    def compraService = (ICompraService)ctx.getBean("compraService")


    public boolean cancele(IResposta resposta) {
        resposta.lida = false
        resposta.mudeEstadoPara(EstadoResposta.CANCELADA)
        compraService.updateRespostaCompra(resposta.id, EstadoResposta.CANCELADA)
        return resposta.estado instanceof RespostaCancelada
    }

    public void recuse(IResposta resposta) {
        resposta.lida = false
        resposta.mudeEstadoPara(EstadoResposta.RECUSADA)
        compraService.updateRespostaCompra(resposta.id, EstadoResposta.RECUSADA)
        cotacaoHandler.dispatch(CotacaoHandler.RECUSA_RESPOSTA, resposta)
    }

    public boolean envie(IResposta resposta){
        resposta.lida = false
        resposta.mudeEstadoPara(EstadoResposta.AGUARDANDO_OUTRAS_RESPOSTAS)
        if(resposta.salve()){
            cotacaoHandler.dispatch(CotacaoHandler.ENVIO_RESPOSTA, resposta, true)
            return true
        }
        return false
    }

    public IResposta salve(IResposta resposta) {
        return resposta.save()
    }

    public String criePlanilhaParaPreenchimentoOffLine(IResposta resposta) {
        return new ExcelExport().exporteFormularioRespostaCotacao(resposta)
    }

    public def importePlanilhaResposta(Resposta resposta, String pathPlanilha) {
        new ExcelImport(new ExcelFile(pathPlanilha)).importePlanilhaResposta(resposta)
    }
}