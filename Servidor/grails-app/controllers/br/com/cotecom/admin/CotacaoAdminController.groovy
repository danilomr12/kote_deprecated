package br.com.cotecom.admin

import br.com.cotecom.domain.cotacao.Cotacao
import br.com.cotecom.domain.cotacao.CotacaoFactory
import br.com.cotecom.domain.usuarios.Comprador
import br.com.cotecom.domain.usuarios.empresa.Endereco
import br.com.cotecom.domain.usuarios.empresa.Cliente
import org.hibernate.criterion.CriteriaSpecification

class CotacaoAdminController {

    def compraService
    def remoteCotacaoService
    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def index = {}

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        params.offset = params.offset? params.offset.toInteger():0
        params.sort = params.sort?:'id'
        params.order = params.order?:'asc'

        def list = Cotacao.createCriteria().list{
            createAlias ('empresa', 'emp', CriteriaSpecification.LEFT_JOIN)
            order (params.sort!="empresa.nomeFantasia"?params.sort:'emp.nomeFantasia', params.order)
            maxResults (params.max)
            firstResult (params.offset)
        }
        [ cotacaoInstanceList: list, cotacaoInstanceTotal: Cotacao.count() ]
    }

    def show = {
        def cotacaoInstance = Cotacao.get( params.id )

        if(!cotacaoInstance) {
            flash.message = "Cotacao not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ cotacaoInstance : cotacaoInstance ] }
    }

    def delete = {
        def cotacaoInstance = Cotacao.get( params.id )
        if(cotacaoInstance) {
            try {
                cotacaoInstance.delete(flush:true)
                flash.message = "Cotacao ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "Cotacao ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "Cotacao not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def cotacaoInstance = Cotacao.get( params.id )

        if(!cotacaoInstance) {
            flash.message = "Cotacao not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ cotacaoInstance : cotacaoInstance ]
        }
    }

    def update = {
        def cotacaoInstance = Cotacao.get( params.id )
        if(cotacaoInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(cotacaoInstance.version > version) {
                    
                    cotacaoInstance.errors.rejectValue("version", "cotacao.optimistic.locking.failure", "Another user has updated this Cotacao while you were editing.")
                    render(view:'edit',model:[cotacaoInstance:cotacaoInstance])
                    return
                }
            }
            cotacaoInstance.properties = params
            if(!cotacaoInstance.hasErrors() && cotacaoInstance.save(flush:true)) {
                flash.message = "Cotacao ${params.id} updated"
                compraService.updateEstadoCompraByCotacaoId(cotacaoInstance.id, cotacaoInstance.codigoEstadoCotacao)
                redirect(action:show,id:cotacaoInstance.id)
            }
            else {
                render(view:'edit',model:[cotacaoInstance:cotacaoInstance])
            }
        }
        else {
            flash.message = "Cotacao not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def create = {
        def cotacaoInstance = new Cotacao()
        cotacaoInstance.properties = params
        return ['cotacaoInstance':cotacaoInstance]
    }

    def save = {
        def cotacaoInstance = CotacaoFactory.crie(params.titulo, params.mensagem, params.dataEntrega, params.dataValidade,
        params.prazoPagamento, Cliente.get(params?.empresa?.id),Endereco.get(params.enderecoEntrega.id))
        if(!cotacaoInstance.hasErrors() && cotacaoInstance.save(flush:true)) {
            compraService.updateEstadoCompraByCotacaoId(cotacaoInstance.id, cotacaoInstance.codigoEstadoCotacao)
            flash.message = "Cotacao ${cotacaoInstance.id} created"
            redirect(action:show,id:cotacaoInstance.id)
        }
        else {
            render(view:'create',model:[cotacaoInstance:cotacaoInstance])
        }
    }

    def downloadPlanilhaExemplo = {
        redirect(url:remoteCotacaoService.donwloadPlanilhaCriacaoCotacaoRascunhoExemplo())
    }
}
