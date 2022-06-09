package br.com.cotecom.domain.cotacao

import br.com.cotecom.util.documents.excel.ExcelExport
import br.com.analise.service.ICompraService
import org.springframework.context.ApplicationContext
import org.codehaus.groovy.grails.commons.ApplicationHolder

public class CotacaoAguardandoPedidos extends EstadoCotacao {

    ApplicationContext ctx = (ApplicationContext)ApplicationHolder.getApplication().getMainContext();
    def compraService = (ICompraService)ctx.getBean("compraService")

    public boolean salve(ICotacao cotacao) {
        if (cotacao.save())
            return true
        return false
    }

    public void gereFalta(ICotacao cotacao) {
        throw new UnsupportedOperationException();
    }

    public void finalize(ICotacao cotacao) {
        compraService.updateEstadoCompraByCotacaoId(cotacao.id, EstadoCotacao.FINALIZADA)
        cotacao.mudeEstadoPara(EstadoCotacao.FINALIZADA)
        cotacao.save()
    }

    public String exporteExcelAnalise(ICotacao cotacao) {
        return new ExcelExport().exporteAnalise(compraService.getCompraByCotacaoId(cotacao.id))
    }
}