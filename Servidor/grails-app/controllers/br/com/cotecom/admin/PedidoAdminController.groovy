

package br.com.cotecom.admin

import br.com.cotecom.domain.pedido.Pedido

class PedidoAdminController {
    
    def index = { redirect(action:list,params:params) }
    def notifierService
    def compraService

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ pedidoInstanceList: Pedido.list( params ), pedidoInstanceTotal: Pedido.count() ]
    }

    def show = {
        def pedidoInstance = Pedido.get( params.id )

        if(!pedidoInstance) {
            flash.message = "Pedido not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ pedidoInstance : pedidoInstance ] }
    }

    def delete = {
        def pedidoInstance = Pedido.get( params.id )
        if(pedidoInstance) {
            try {
                pedidoInstance.delete(flush:true)
                flash.message = "Pedido ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "Pedido ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "Pedido not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def pedidoInstance = Pedido.get( params.id )

        if(!pedidoInstance) {
            flash.message = "Pedido not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ pedidoInstance : pedidoInstance ]
        }
    }

    def update = {
        def pedidoInstance = Pedido.get( params.id )
        if(pedidoInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(pedidoInstance.version > version) {
                    
                    pedidoInstance.errors.rejectValue("version", "pedido.optimistic.locking.failure", "Another user has updated this Pedido while you were editing.")
                    render(view:'edit',model:[pedidoInstance:pedidoInstance])
                    return
                }
            }
            pedidoInstance.properties = params
            if(!pedidoInstance.hasErrors() && pedidoInstance.save(flush:true)) {
                flash.message = "Pedido ${params.id} updated"
                redirect(action:show,id:pedidoInstance.id)
            }
            else {
                render(view:'edit',model:[pedidoInstance:pedidoInstance])
            }
        }
        else {
            flash.message = "Pedido not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def create = {
        def pedidoInstance = new Pedido()
        pedidoInstance.properties = params
        return ['pedidoInstance':pedidoInstance]
    }

    def save = {
        def pedidoInstance = new Pedido(params)
        if(!pedidoInstance.hasErrors() && pedidoInstance.save(flush:true)) {
            flash.message = "Pedido ${pedidoInstance.id} created"
            redirect(action:show,id:pedidoInstance.id)
        }
        else {
            render(view:'create',model:[pedidoInstance:pedidoInstance])
        }
    }

    def reenviePedido = {
        Long pedidoId = params.pedidoId as Long
        Pedido pedido = Pedido.get(pedidoId)
        def compra = compraService.getCompra(pedido?.resposta?.cotacao?.compraId)
        def respostaCompra = compra.respostasCompra.find{it.emailRepresentante == pedido.resposta.representante.email}
        def empresa = pedido.resposta.cotacao.empresa
        def comprador = empresa.comprador
        if(notifierService.notifiquePedidoParaRepresentante(pedido,respostaCompra, comprador.nome, comprador.email,comprador.id, empresa.nomeFantasia)){
            flash.message = "Pedido enviado para o email ${pedido.resposta.representante.email}"
        }else{
            flash.message = "Pedido: id -> ${pedido?.id}, não enviado ao representante"
        }
        render view: 'show', model:[pedidoInstance: Pedido.get(pedidoId)]
    }
}
