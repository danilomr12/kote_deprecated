package br.com.cotecom.admin
import br.com.cotecom.domain.resposta.Resposta
import br.com.cotecom.domain.resposta.EstadoResposta

class RespostaAdminController {

    def index = { redirect(action:list,params:params) }
    def cotacaoService
    def compraService

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ respostaInstanceList: Resposta.list( params ), respostaInstanceTotal: Resposta.count() ]
    }

    def show = {
        def respostaInstance = Resposta.get( params.id )

        if(!respostaInstance) {
            flash.message = "Resposta not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ respostaInstance : respostaInstance ] }
    }

    def delete = {
        def respostaInstance = Resposta.get( params.id )
        if(respostaInstance) {
            try {
                respostaInstance.delete(flush:true)
                flash.message = "Resposta ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "Resposta ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "Resposta not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def respostaInstance = Resposta.get( params.id )

        if(!respostaInstance) {
            flash.message = "Resposta not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ respostaInstance : respostaInstance ]
        }
    }

    def update = {
        def respostaInstance = Resposta.get( params.id )
        if(respostaInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(respostaInstance.version > version) {

                    respostaInstance.errors.rejectValue("version", "resposta.optimistic.locking.failure", "Another user has updated this Resposta while you were editing.")
                    render(view:'edit',model:[respostaInstance:respostaInstance])
                    return
                }
            }
            respostaInstance.properties = params
            if(!respostaInstance.hasErrors() && respostaInstance.save(flush:true)) {
                compraService.updateRespostaCompra(respostaInstance.id, respostaInstance.codigoEstado)
                flash.message = "Resposta ${params.id} updated"
                redirect(action:show,id:respostaInstance.id)
            }
            else {
                render(view:'edit',model:[respostaInstance:respostaInstance])
            }
        }
        else {
            flash.message = "Resposta not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def create = {
        def respostaInstance = new Resposta()
        respostaInstance.properties = params
        return ['respostaInstance':respostaInstance]
    }

    def save = {
        def respostaInstance = new Resposta(params)
        if(!respostaInstance.hasErrors() && respostaInstance.save(flush:true)) {
            compraService.updateRespostaCompra(respostaInstance.id, respostaInstance.codigoEstado)
            flash.message = "Resposta ${respostaInstance.id} created"
            redirect(action:show,id:respostaInstance.id)
        }
        else {
            render(view:'create',model:[respostaInstance:respostaInstance])
        }
    }

    def reenvieEmailCotacao = {
        Long respostaId = params.respostaId as Long
        if(cotacaoService.reenvieEmailCotacao(respostaId)){
            flash.message = "e-mail enviado ao representante ${Resposta.get(params.respostaId)?.representante?.email}"
        }else{
            flash.message = "e-mail da resposta: ${params.respostaId}não enviado, tente novamente mais tarde"
        }
        render view:'show', model: [respostaInstance: Resposta.get(respostaId)]
    }
}
