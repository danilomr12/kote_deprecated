

package br.com.cotecom.admin

import br.com.cotecom.domain.usuarios.Estoquista
import br.com.cotecom.domain.usuarios.Representante
import br.com.cotecom.domain.usuarios.Usuario
import br.com.cotecom.domain.usuarios.empresa.Cliente
import br.com.cotecom.domain.usuarios.empresa.CoteCom
import br.com.cotecom.domain.usuarios.empresa.Empresa
import br.com.cotecom.domain.usuarios.empresa.Fornecedor
import com.sun.corba.se.spi.activation._InitialNameServiceImplBase

class EmpresaAdminController {
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']
    def compraService

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ empresaInstanceList: Empresa.list( params ), empresaInstanceTotal: Empresa.count() ]
    }

    def show = {
        def empresaInstance = Empresa.get( params.id )

        if(!empresaInstance) {
            flash.message = "Empresa not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ empresaInstance : empresaInstance ] }
    }

    def delete = {
        def empresaInstance = Empresa.get( params.id )
        if(empresaInstance) {
            try {
                empresaInstance.delete(flush:true)
                flash.message = "Empresa ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "Empresa ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "Empresa not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def empresaInstance = Empresa.get( params.id )

        if(!empresaInstance) {
            flash.message = "Empresa not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ empresaInstance : empresaInstance ]
        }
    }

    def update = {
        def empresaInstance = Empresa.get( params.id )
        if(empresaInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(empresaInstance.version > version) {
                    
                    empresaInstance.errors.rejectValue("version", "empresa.optimistic.locking.failure", "Another user has updated this Empresa while you were editing.")
                    render(view:'edit',model:[empresaInstance:empresaInstance])
                    return
                }
            }
            empresaInstance.properties = params
            if(!empresaInstance.hasErrors() && empresaInstance.save(flush:true)) {
                flash.message = "Empresa ${params.id} updated"
                redirect(action:show,id:empresaInstance.id)
            }
            else {
                render(view:'edit',model:[empresaInstance:empresaInstance])
            }
        }
        else {
            flash.message = "Empresa not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def create = {
        def empresaInstance = new Empresa()
        empresaInstance.properties = params
        return ['empresaInstance':empresaInstance]
    }

    def save = {
        def empresaInstance
        empresaInstance = createCorrectEmpresa(empresaInstance)

        if(!empresaInstance.hasErrors() && empresaInstance.save(flush:true)) {
            flash.message = "Empresa ${empresaInstance.id} created"
            redirect(action:show,id:empresaInstance.id)
        }
        else {
            render(view:'create',model:[empresaInstance:empresaInstance])
        }
    }

    def showResumoCompras = {
        if (params.id && Cliente.exists(params.id) && params.dataFim && params.dataInicio){
            def clienteInstance = Cliente.read(params.id)

            double totalDasComprasNoMes = getTotalComprasDoPeriodo(params.dataInicio, params.dataFim, clienteInstance)
            render(view: 'cobranca', model: [totalDasComprasNoMes: totalDasComprasNoMes?.round(2), empresaInstance: clienteInstance,
                    dataFim: params.dataFim, dataInicio: params.dataInicio])
        }else{
            render(view: 'cobranca', model: [empresaInstance: Cliente.get(params.id)])
        }
    }

    private double getTotalComprasDoPeriodo(Date dataInicio, Date dataFim, Cliente clienteInstance) {
        Double totalDasComprasNoMes = compraService.getTotalComprasDoPeriodo(dataInicio, dataFim, clienteInstance.id as Long)
        totalDasComprasNoMes
    }

    def gerarCobrancasDeTodosClientes = {
        def inicio = params.dataInicio
        def fim = params.dataFim
        def list = [:]
        Cliente.list().each{ Cliente cliente ->
            list.put("${cliente.id}" , compraService.getTotalComprasDoPeriodo(inicio, fim, cliente.id))
        }

        render(view: "/admin/index", model: [result: list, dataFim: fim, dataInicio: inicio])
    }

    def buscar = {
        if(!params?.busca)
            redirect(action: 'list')
        def busca = "%" + params?.busca + "%"
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        def list = Empresa.findAllByNomeFantasiaIlikeOrRazaoSocialIlike(busca,busca, params)
        def count = Empresa.countByNomeFantasiaIlikeOrRazaoSocialIlike(busca,busca, params)
        render(view: 'list', model:[empresaInstanceList: list, empresaInstanceTotal:  count, busca: params?.busca])
    }
    
    private Empresa createCorrectEmpresa(empresaInstance) {
        switch (params.tipo) {
            case Empresa.CLIENTE:
                empresaInstance = new Cliente(params)
                break
            case Empresa.FORNECEDOR:
                empresaInstance = new Fornecedor(params)
                break
            case Empresa.KOTE:
                empresaInstance = new CoteCom(params)
                break

        }
        empresaInstance
    }
}
