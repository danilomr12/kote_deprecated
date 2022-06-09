package br.com.cotecom.domain.resposta

import br.com.analise.domain.Compra
import br.com.analise.service.ICompraService
import br.com.cotecom.domain.cotacao.AnaliseFactory
import br.com.cotecom.domain.cotacao.Cotacao
import br.com.cotecom.domain.cotacao.EstadoCotacao
import org.apache.log4j.Logger
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.hibernate.StatelessSession
import org.hibernate.Transaction

public class RespostaEmAnalise extends EstadoResposta {

    private static final log = Logger.getLogger(RespostaEmAnalise.class)

    def ctx = ApplicationHolder.application.mainContext
    def sessionFactory = ctx.sessionFactory
    def compraService = (ICompraService)ctx.getBean("compraService")


    public IResposta salve(IResposta resposta) {
        return resposta.save()
    }

    public boolean cancele(IResposta resposta) {
        StatelessSession newSession = sessionFactory.openStatelessSession();
        Transaction tx = newSession.beginTransaction();
        Resposta sessionResposta = Resposta.get(resposta.id)
        try{
            sessionResposta.mudeEstadoPara(EstadoResposta.CANCELADA)
            compraService.updateRespostaCompra(resposta.id, EstadoResposta.CANCELADA)
            compraService.removeRespostasFromItens(resposta.cotacao.id, sessionResposta.id)
        } catch (Exception e){
            sessionResposta.mudeEstadoPara(EstadoResposta.EM_ANALISE)
            compraService.updateRespostaCompra(resposta.id, EstadoResposta.EM_ANALISE)
            new AnaliseFactory().crieItensRespostaCompra(resposta.id)
            log.debug e
        } finally {
            tx.commit()
            newSession.close()
        }
        return resposta.estado instanceof RespostaCancelada
    }

}