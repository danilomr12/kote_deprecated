package br.com.cotecom.domain.usuarios

import br.com.cotecom.domain.cotacao.ICotacao
import br.com.cotecom.domain.resposta.Resposta

class Representante extends Usuario {

    def possuiRespostaDaCotacao(ICotacao cotacao) {
        String sql = "from Resposta where cotacao_id = '${cotacao.id}' and representante_id = '${this.id}'"
        def result = Resposta.executeQuery(sql)
        if(result == null || result.size() == 0)
            return false
        return true
    }

    public String toString(){
        super.toString()
    }

}