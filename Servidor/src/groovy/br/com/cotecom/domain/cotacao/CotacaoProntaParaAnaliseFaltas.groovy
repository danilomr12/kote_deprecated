package br.com.cotecom.domain.cotacao

import br.com.cotecom.domain.analise.Analise
import br.com.cotecom.util.documents.excel.ExcelExport
import org.hibernate.FetchMode

public class CotacaoProntaParaAnaliseFaltas extends EstadoCotacao {

	public void gereCotacaoDeFalta(ICotacao cotacao) {
		throw new UnsupportedOperationException();
	}

    public boolean salve(ICotacao cotacao) {
        if(cotacao.save())
            return true
        return false
    }

    public void finalize(ICotacao cotacao) {
		throw new UnsupportedOperationException();
	}

    public String exporteExcelAnalise(ICotacao cotacao) {
        return new ExcelExport().exporteAnalise(cotacao.analise)
    }

    public Analise getAnalise(ICotacao cotacao){
        // TODO testar performance
        Analise.withCriteria {
            eq 'id', cotacao.id
            fetchMode "itens", FetchMode.SELECT
        }
                .get(0)
    }

}