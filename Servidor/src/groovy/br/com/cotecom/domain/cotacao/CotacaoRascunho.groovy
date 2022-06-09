package br.com.cotecom.domain.cotacao

import org.codehaus.groovy.grails.commons.ApplicationHolder as AH

import br.com.cotecom.domain.resposta.RespostaFactory
import br.com.cotecom.domain.usuarios.Representante
import br.com.cotecom.services.NotifierService
import org.apache.log4j.Logger
import br.com.analise.service.ICompraService

public class CotacaoRascunho extends EstadoCotacao {

    private static final log = Logger.getLogger(CotacaoRascunho.class)

    NotifierService notifierService

    def ctx = AH.application.mainContext
    def dataService = ctx.dataService
    def backgroundService = ctx.backgroundService
    def sessionFactory = ctx.sessionFactory
    ICompraService compraService = (ICompraService)ctx.getBean("compraService")

    public boolean envie(ICotacao cotacao, List<Representante> representantes) {
        boolean resultado = true
        try {
            log.debug("Enviando cotação - id: ${cotacao.id}}")
            dataService.refreshSession()
            cotacao.mudeEstadoPara(EstadoCotacao.PROCESSANDO_ENVIO_RESPOSTAS)
            compraService.updateEstadoCompraByCotacaoId(cotacao.id, EstadoCotacao.PROCESSANDO_ENVIO_RESPOSTAS)
            cotacao.save()
            def session = sessionFactory.getCurrentSession()
            session.flush()
            session.clear()
            doBackgroungWork(representantes.collect {it.id}, cotacao.id)
            log.debug("disparado background work de cotação - id: ${cotacao.id}}")
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("Erro ao enviar cotação - " + " id: {$cotacao?.id}" + e.toString())
        }
        return resultado
    }

    private void doBackgroungWork(List idRepresentantes, Long idCotacao) {
        backgroundService.execute("Criando Analise e respostas de cotação - id: ${idCotacao}", {
            new AnaliseFactory().crie(idCotacao)
            new RespostaFactory().crie(idRepresentantes, idCotacao)
        })
    }

    public boolean salve(ICotacao cotacao) {
        if (cotacao.save())
            return true
        return false
    }

    public boolean cancele(Cotacao cotacao) {
        def idCotacao = cotacao.id
        if (cotacao.itens.size() <= 0) {
            cotacao.delete()
            return Cotacao.get(idCotacao) == null
        }
        cotacao.mudeEstadoPara(EstadoCotacao.CANCELADA)
        compraService.updateEstadoCompraByCotacaoId(cotacao.id, EstadoCotacao.CANCELADA)
        if (cotacao.save())
            return true
        false
    }
}