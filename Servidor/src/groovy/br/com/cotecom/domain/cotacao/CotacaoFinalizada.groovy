package br.com.cotecom.domain.cotacao

import br.com.cotecom.util.documents.excel.ExcelExport
import org.springframework.context.ApplicationContext
import org.codehaus.groovy.grails.commons.ApplicationHolder
import br.com.analise.service.ICompraService

public class CotacaoFinalizada extends EstadoCotacao {

    ApplicationContext ctx = (ApplicationContext)ApplicationHolder.getApplication().getMainContext();
    def compraService = (ICompraService)ctx.getBean("compraService")

    public boolean salve(ICotacao cotacao) {
        if (cotacao.save())
            return true
        return false
    }

    public String exporteExcelAnalise(ICotacao cotacao) {
        return new ExcelExport().exporteAnalise(compraService.getCompraByCotacaoId(cotacao.id))
    }
}