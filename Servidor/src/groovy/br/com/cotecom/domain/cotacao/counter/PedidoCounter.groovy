package br.com.cotecom.domain.cotacao.counter

import br.com.cotecom.domain.resposta.IResposta
import br.com.cotecom.domain.resposta.EstadoResposta
import br.com.cotecom.domain.resposta.Resposta

public class PedidoCounter extends AbstractCounter{

    PedidoCounter(cotacao){
        this.cotacao = cotacao

        List<IResposta> respostasRespondidas = cotacao.respostas.findAll{
            it.codigoEstado != EstadoResposta.CANCELADA && it.codigoEstado != EstadoResposta.RECUSADA &&
                    it.codigoEstado != EstadoResposta.PERDIDA
        } as List<Resposta>

        if(!respostasRespondidas)
            this.recebido = 0
        else
            this.recebido = respostasRespondidas.findAll {it.codigoEstado == EstadoResposta.PEDIDO_FATURADO}.size()
        this.faltando = respostasRespondidas.size() - recebido
    }

}