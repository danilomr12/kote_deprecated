package br.com.cotecom.domain.cotacao

import br.com.cotecom.domain.cotacao.counter.AbstractCounter
import br.com.cotecom.domain.cotacao.counter.RespostaCounter
import br.com.cotecom.domain.resposta.IResposta
import br.com.cotecom.domain.resposta.RespostaFactory
import br.com.cotecom.domain.usuarios.Representante
import org.apache.log4j.Logger
import org.codehaus.groovy.grails.commons.ApplicationHolder
import br.com.analise.service.ICompraService
import br.com.cotecom.domain.resposta.Resposta
import br.com.cotecom.domain.resposta.EstadoResposta

public class CotacaoProntaParaAnalise extends EstadoCotacao {

    private static final log = Logger.getLogger(CotacaoProntaParaAnalise.class)
    def ctx = ApplicationHolder.application.mainContext
    def dataService = ctx.dataService
    def backgroundService = ctx.backgroundService
    def compraService = (ICompraService)ctx.getBean("compraService")

    public boolean cancele(ICotacao cotacao){
        cotacao.mudeEstadoPara(EstadoCotacao.CANCELADA)
        compraService.updateEstadoCompraByCotacaoId(cotacao.id, EstadoCotacao.CANCELADA)
        cotacao.respostas.each { IResposta resposta ->
            resposta.cancele()
        }
        return cotacao.save()
    }

    public void analisar(ICotacao cotacao) {
        cotacao.mudeEstadoPara(EM_ANALISE)
        cotacao.save()
        compraService.updateEstadoCompraByCotacaoId(cotacao.id, EstadoCotacao.EM_ANALISE)
        cotacao.respostas.findAll {Resposta resposta ->
            resposta.codigoEstado != EstadoResposta.CANCELADA && resposta.codigoEstado != EstadoResposta.PERDIDA &&
                    resposta.codigoEstado != EstadoResposta.RECUSADA
        }.each {Resposta resposta ->
            resposta.mudeEstadoPara(EstadoResposta.EM_ANALISE)
            resposta.save()
            compraService.updateRespostaCompra(resposta.id, EstadoResposta.EM_ANALISE)
        }
    }

    public void aguardeAnalise(ICotacao cotacao){
        cotacao.mudeEstadoPara(EstadoCotacao.PRONTA_PARA_ANALISE)
        compraService.updateEstadoCompraByCotacaoId(cotacao.id, EstadoCotacao.PRONTA_PARA_ANALISE)
        cotacao.save()
    }

    public AbstractCounter makeRespostaCounter(ICotacao cotacao){
        return new RespostaCounter(cotacao)
    }

    public boolean envie(ICotacao cotacao, List<Representante> representantes) {
        boolean resultado = true
        try {
            dataService.refreshSession()
            def idsRepresentantes = []
            representantes.each {
                if(!it.possuiRespostaDaCotacao(cotacao)){
                    idsRepresentantes << it.id
                }
            }
            cotacao.mudeEstadoPara(EstadoCotacao.PROCESSANDO_ENVIO_RESPOSTAS)
            compraService.updateEstadoCompraByCotacaoId(cotacao.id, EstadoCotacao.PROCESSANDO_ENVIO_RESPOSTAS)
            cotacao.save()
            doBackgroungWork(idsRepresentantes, cotacao.id)
        }
        catch (Exception e) {
            e.printStackTrace()
            resultado = false
        }
        return resultado
    }

    private void doBackgroungWork(List idRepresentantes, Long idCotacao) {
        backgroundService.execute("Criando respostas", {
            new RespostaFactory().crie(idRepresentantes, idCotacao)
        })
    }

    public boolean salve(ICotacao cotacao) {
        if(cotacao.save())
            return true
        return false
    }

}