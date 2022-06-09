package br.com.cotecom.domain.cotacao

import br.com.cotecom.domain.analise.Analise
import org.hibernate.FetchMode

public class CotacaoCancelada extends EstadoCotacao {

    public boolean salve(ICotacao cotacao){
        if(cotacao.save(flush:true,validate:false))
            return true
        return false
    }

    public Analise getAnalise(ICotacao cotacao){
        // TODO testar performance
        Analise analise = Analise.withCriteria {
            eq 'id', cotacao.id
            fetchMode "itens", FetchMode.SELECT
        }.get(0)
        return analise
    }

}