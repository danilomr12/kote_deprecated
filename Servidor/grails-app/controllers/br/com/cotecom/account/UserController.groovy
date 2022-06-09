package br.com.cotecom.account

import br.com.cotecom.domain.usuarios.Comprador
import br.com.cotecom.domain.usuarios.Usuario
import br.com.cotecom.domain.usuarios.Responsabilidade

class UserController {

    def index = { }

    def show = {
        def usuarioInstanve = Usuario.get( params.id )

        if(!usuarioInstanve) {
            flash.message = "Usuário not found with id ${params.id}"
            redirect(action:index)
        }
        else { return [ compradorInstance : usuarioInstanve ] }
    }

    def delete = {
        def usuarioInstance = Usuario.get( params.id )
        if(usuarioInstance) {
            try {
                usuarioInstance.delete(flush:true)
                flash.message = "Usuario ${params.id} deleted"
                redirect(action:index)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "Usuario ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "Usuario not found with id ${params.id}"
            redirect(action:index)
        }
    }

    def save = {

        def compradorInstance = new Comprador(params)
        compradorInstance.password = springSecurityService.encodePassword(params.password)
        if(!compradorInstance.hasErrors() && compradorInstance.save(flush:true)) {
            compradorInstance.addResponsabilidade(Responsabilidade.findByResponsabilidade("ROLE_COMPRADOR"))
            flash.message = "Comprador ${compradorInstance.id} created"
            redirect(action:show,id:compradorInstance.id)
        }
        else {
            render(view:'create',model:[compradorInstance:compradorInstance])
        }
    }

    def update = {
        def compradorInstance = Comprador.get( params.id )
        if(compradorInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(compradorInstance.version > version) {
                    compradorInstance.errors.rejectValue("version", "comprador.optimistic.locking.failure", "Another user has updated this Comprador while you were editing.")
                    render(view:'edit',model:[compradorInstance:compradorInstance])
                    return
                }
            }
            if(params.password == null || params.password == ""){
                params.remove('password')
                compradorInstance.properties = params
            }else{
                compradorInstance.properties = params
                compradorInstance.password = springSecurityService.encodePassword(params.password)
            }
            if(!compradorInstance.responsabilidades.contains(Responsabilidade.findByResponsabilidade("ROLE_COMPRADOR")))
                compradorInstance.addResponsabilidade(Responsabilidade.findByResponsabilidade("ROLE_COMPRADOR"))
            if(!compradorInstance.hasErrors() && compradorInstance.save(flush:true)) {
                flash.message = "Comprador ${params.id} updated"
                redirect(action:show,id:compradorInstance.id)
            }
            else {
                render(view:'edit',model:[compradorInstance:compradorInstance])
            }
        }
        else {
            flash.message = "Comprador not found with id ${params.id}"
            redirect(action:list)
        }
    }


}
