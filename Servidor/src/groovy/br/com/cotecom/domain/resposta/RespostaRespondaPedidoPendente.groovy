package br.com.cotecom.domain.resposta

import br.com.analise.service.ICompraService

import org.codehaus.groovy.grails.commons.ApplicationHolder
import br.com.cotecom.domain.cotacao.CotacaoHandler

public class RespostaRespondaPedidoPendente extends EstadoResposta{

    def ctx = ApplicationHolder.application.mainContext
    def compraService = (ICompraService)ctx.getBean("compraService")

    public IResposta salve(IResposta resposta) {
        return resposta.save()
    }

    public Boolean faturePedido(IResposta resposta){
        resposta.mudeEstadoPara(EstadoResposta.PEDIDO_FATURADO)
        boolean ok = true
        try{
            def result = resposta.salve()
            if(result){
                def envioPedido = cotacaoHandler.dispatch(CotacaoHandler.ENVIO_PEDIDO, resposta)
                compraService.updateRespostaCompra(resposta.id, EstadoResposta.PEDIDO_FATURADO)
            }else{
                ok = false
            }
        }catch (Exception e){
            e.printStackTrace()
            ok = false
            compraService.updateRespostaCompra(resposta.id, EstadoResposta.RESPONDA_PEDIDO_PENDENTE)
        }finally{
            return ok
        }
    }
}