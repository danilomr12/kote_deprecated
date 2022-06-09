package br.com.cotecom.admin

import br.com.cotecom.domain.usuarios.Administrador
import br.com.cotecom.domain.usuarios.Comprador
import br.com.cotecom.domain.usuarios.Estoquista
import br.com.cotecom.domain.usuarios.Representante
import br.com.cotecom.domain.usuarios.Responsabilidade
import br.com.cotecom.domain.usuarios.Supervisor
import br.com.cotecom.domain.usuarios.Usuario
import org.hibernate.criterion.CriteriaSpecification

class UsuarioAdminController {

    def index = { redirect(action: list, params: params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete: 'POST', save: 'POST', update: 'POST']

    def springSecurityService

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        params.offset = params.offset? params.offset.toInteger():0
        params.sort = params.sort?:'id'
        params.order = params.order?:'asc'

        def list = Usuario.createCriteria().list{
            createAlias ('empresa', 'emp', CriteriaSpecification.LEFT_JOIN)
            order (params.sort!="empresa.nomeFantasia"?params.sort:'emp.nomeFantasia', params.order)
            maxResults (params.max)
            firstResult (params.offset)
        }
        [usuarioInstanceList: list, usuarioInstanceTotal: Usuario.count()]
    }

    def show = {
        def usuarioInstance = Usuario.get(params.id)

        if (!usuarioInstance) {
            flash.message = "Usuario not found with id ${params.id}"
            redirect(action: list)
        } else {
            return [usuarioInstance: usuarioInstance]
        }
    }

    def delete = {
        def usuarioInstance = Usuario.get(params.id)
        if (usuarioInstance) {
            try {
                usuarioInstance.delete(flush: true)
                flash.message = "Usuario ${params.id} deleted"
                redirect(action: list)
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "Usuario ${params.id} could not be deleted"
                redirect(action: show, id: params.id)
            }
        } else {
            flash.message = "Usuario not found with id ${params.id}"
            redirect(action: list)
        }
    }

    def edit = {
        def usuarioInstance = Usuario.get(params.id)
        if (!usuarioInstance) {
            flash.message = "Usuario not found with id ${params.id}"
            redirect(action: list)
        } else {
            return [usuarioInstance: usuarioInstance]
        }
    }

    def update = {
        def usuarioInstance = Usuario.get(params.id)
        if (usuarioInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (usuarioInstance.version > version) {

                    usuarioInstance.errors.rejectValue("version", "usuario.optimistic.locking.failure", "Another user has updated this Usuario while you were editing.")
                    render(view: 'edit', model: [usuarioInstance: usuarioInstance])
                    return
                }
            }
            if(params.password == null || params.password == ""){
                params.remove('password')
                usuarioInstance.properties = params
            }else{
                usuarioInstance.properties = params
                usuarioInstance.password = springSecurityService.encodePassword(params.password)
            }

            if (!usuarioInstance.hasErrors() && usuarioInstance.save(flush: true)) {
                flash.message = "Usuario ${params.id} updated"
                redirect(action: show, id: usuarioInstance.id)
            } else {
                render(view: 'edit', model: [usuarioInstance: usuarioInstance])
            }
        } else {
            flash.message = "Usuario not found with id ${params.id}"
            redirect(action: list)
        }
    }

    def create = {
        def usuarioInstance = new Usuario()
        usuarioInstance.properties = params
        return ['usuarioInstance': usuarioInstance, tipo: params.tipo]
    }

    def save = {
        def usuarioInstance
        usuarioInstance = createCorrectUser(usuarioInstance)
        usuarioInstance.password = springSecurityService.encodePassword(params.password)

        if (usuarioInstance && !usuarioInstance.hasErrors() && usuarioInstance?.save(flush: true)) {
            flash.message = "Usuario ${usuarioInstance.id} created"
            setRoles(usuarioInstance)
            redirect(action: show, id: usuarioInstance.id)
        } else {
            render(view: 'create', model: [usuarioInstance: usuarioInstance])
        }
    }

    def setRoles(Usuario usuarioInstance) {
        switch (params.tipo) {
            case Usuario.COMPRADOR:
                usuarioInstance.addResponsabilidade(Responsabilidade.findByResponsabilidade("ROLE_COMPRADOR"))
                break
            case Usuario.REPRESENTANTE:
                usuarioInstance.addResponsabilidade(Responsabilidade.findByResponsabilidade("ROLE_REPRESENTANTE"))
                break
            case Usuario.ESTOQUISTA:
                usuarioInstance.addResponsabilidade(Responsabilidade.findByResponsabilidade("ROLE_ESTOQUISTA"))
                break
            case Usuario.ADMINISTRADOR:
                usuarioInstance.addResponsabilidade(Responsabilidade.findByResponsabilidade("ROLE_ADMIN"))
                break
        }
        usuarioInstance
    }

    private Usuario createCorrectUser(usuarioInstance) {
        switch (params.tipo) {
            case Usuario.COMPRADOR:
                usuarioInstance = new Comprador(params)
                break
            case Usuario.REPRESENTANTE:
                usuarioInstance = new Representante(params)
                break
            case Usuario.ESTOQUISTA:
                usuarioInstance = new Estoquista(params)
                break
            case Usuario.ADMINISTRADOR:
                usuarioInstance = new Administrador(params)
                break
            case Usuario.SUPERVISOR:
                usuarioInstance = new Supervisor(params)
                break
        }
        usuarioInstance
    }

    def buscar = {
        if(!params?.busca)
            redirect(action: 'list')
        def busca = "%" + params?.busca + "%"
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        def list = Usuario.findAllByNomeIlikeOrEmailIlike(busca,busca, params)
        def count = Usuario.countByNomeLikeOrEmailIlike(busca,busca, params)
        render(view: 'list', model:[usuarioInstanceList: list, usuarioInstanceTotal: count, busca: params?.busca])
    }
}
