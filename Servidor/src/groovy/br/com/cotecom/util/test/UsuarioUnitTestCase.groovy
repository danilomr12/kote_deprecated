package br.com.cotecom.util.test

import br.com.cotecom.domain.usuarios.Comprador
import br.com.cotecom.domain.usuarios.Representante
import br.com.cotecom.domain.usuarios.Supervisor
import grails.test.GrailsUnitTestCase
import br.com.cotecom.domain.usuarios.empresa.*

public class UsuarioUnitTestCase extends GrailsUnitTestCase{

    Fornecedor fornecedor
    Cliente cliente

    Representante representante
    Representante representanteNull
    Representante representanteInvalido

    Comprador comprador
    Comprador compradorNull
    Comprador compradorInvalido

    Supervisor supervisor
    Supervisor supervisorNull
    Supervisor supervisorInvalido

    Atendimento atendimento

    void setUp(){
        fornecedor = new Fornecedor(nomeFantasia: "Megafort", razaoSocial:"Foo Fighters Yeah Baby",
                cnpj: "11.233.222/0001-11", email:"megafort@cotecom.com.br",ramo: new Ramo(tipo: 0))
        fornecedor.save(flush:true)

        cliente = new Cliente(nomeFantasia: "Supermercado Bolinha", razaoSocial: "Comercial Eye Of The Tiger",
                cnpj: "34.877.343/0001-24", email:"bolinha@cotecom.com.br",ramo: new Ramo(tipo: 2))
        cliente.save(flush:true)

        supervisor = new Supervisor(nome: "Cassio Felipe", email: "cassio@cotecom.com.br", password: "pass2pass")
        supervisor.addTelefone(new Telefone(tipo: 0, ddd: "062", numero:"75543734"))
        supervisor.save(flush:true)
        fornecedor.addUsuario(supervisor)
        supervisorNull = null
        supervisorInvalido = new Supervisor(id:"Id_Invalido")

        representante = new Representante(nome: "Danilo Marques", email: "danilo@cotecom.com.br", password: "xyz123xyz",
                supervisor: supervisor)
        representante.addTelefone(new Telefone(tipo: 0, ddd: "062", numero:"87654312"))
        representante.save(flush:true)
        fornecedor.addUsuario(representante)
        representanteNull = null
        representanteInvalido = new Representante(id:"Id_Invalido")

        comprador = new Comprador(nome:"Ricardo Gobbo", email: "ricardo@cotecom.com.br", password: "xyz123xyz" )
        comprador.addTelefone(new Telefone(tipo: 0, ddd: "062", numero:"44938332"))
        comprador.save(flush:true)
        cliente.addUsuario(comprador)
        compradorNull = null
        compradorInvalido = new Comprador(id:"Id_Invalido")

        atendimento = new Atendimento(fornecedor: fornecedor, cliente: cliente, representante: representante)
        atendimento.save(flush:true)

    }

}