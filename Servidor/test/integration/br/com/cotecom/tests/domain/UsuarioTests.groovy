package br.com.cotecom.tests.domain

import br.com.cotecom.domain.usuarios.Comprador
import br.com.cotecom.domain.usuarios.Representante
import br.com.cotecom.domain.usuarios.Supervisor
import br.com.cotecom.domain.usuarios.empresa.Cliente
import br.com.cotecom.domain.usuarios.empresa.Endereco
import br.com.cotecom.domain.usuarios.empresa.Fornecedor
import br.com.cotecom.domain.usuarios.empresa.Telefone
import br.com.cotecom.util.fixtures.EmpresaFixture
import br.com.cotecom.util.fixtures.UsuarioFixture
import grails.test.GrailsUnitTestCase

public class UsuarioTests extends GrailsUnitTestCase {

    Representante representante
    String email
    Telefone telefone
    Comprador comprador2
    String senha

    void setUp() {
        super.setUp()
        email = EmpresaFixture.crieEmailSimples()
        senha = "123456"
        telefone = new Telefone(ddd:"062", tipo:Telefone.RESIDENCIAL, numero:"32338552")
        Fornecedor empresa = new Fornecedor(email:EmpresaFixture.crieEmailSimples(), nomeFantasia:"Lider Atac.",
                razaoSocial:"Terra Atacado distr. ltda").save(flush:true)
        representante = new Representante(nome:"representante1",password: senha, email: email, empresa: empresa).save(flush:true)
        Cliente cliente = new Cliente(email:EmpresaFixture.crieEmailSimples(), nomeFantasia:"supermercador xx",
                razaoSocial:"xx ltda").save(flush:true)
        comprador2 = new Comprador(nome:"alberto", email: EmpresaFixture.crieEmailSimples(), empresa: cliente, password:senha).save(flush:true)
    }

    void tearDown(){
        super.tearDown()
    }

    void testCadastroNovoUsuario(){
        // Criar e salvar um comprador junto com uma empresa e automaticamente dar a ele a supervisor padr�o

    }

    void testAdicioneTelefone() {
        representante.addTelefone(new Telefone(ddd:"062",tipo:Telefone.FAX,numero:"32338552"))
        assertTrue representante.telefones.asList().get(0).ddd.equals("062")
        assertTrue representante.telefones.asList().get(0).tipo.equals(Telefone.FAX)
        assertTrue representante.telefones.asList().get(0).numero.equals("32338552")
    }

    void testAssociacaoEntreSupervidorERepresentante(){
        Fornecedor empresaA = EmpresaFixture.crieFornecedorCompletoComEnderecoETelefone().save(flush:true)
        Fornecedor empresaB = EmpresaFixture.crieFornecedorCompletoComEnderecoETelefone().save(flush:true)

        Supervisor supervisorEmpresaA = UsuarioFixture.crieSupervisorSemEmpresa().save(flush:true)
        empresaA.addUsuario(supervisorEmpresaA)
        Supervisor supervisorEmpresaB = UsuarioFixture.crieSupervisorSemEmpresa().save(flush:true)
        empresaB.addUsuario(supervisorEmpresaB)

        List<Representante> representantesEmpresaA = []
        List<Representante> representantesEmpresaB = []

        10.times {
            Representante representanteA = Representante.build(supervisor:Supervisor.build())
            empresaA.addUsuario(representanteA)
            supervisorEmpresaA.addRepresentante(representanteA)
            representantesEmpresaA << representanteA
            Representante representanteB = Representante.build(supervisor:Supervisor.build())
            empresaB.addUsuario(representanteB)
            supervisorEmpresaB.addRepresentante(representanteB)
            representantesEmpresaB << representanteB
        }

        representantesEmpresaA.each { Representante representanteEmpresaA ->
            assertTrue supervisorEmpresaA.subordinados.contains(representanteEmpresaA)
            assertEquals supervisorEmpresaA, representanteEmpresaA.supervisor
        }
        representantesEmpresaB.each { Representante representanteEmpresaB ->
            assertTrue supervisorEmpresaB.subordinados.contains(representanteEmpresaB)
            assertEquals supervisorEmpresaB, representanteEmpresaB.supervisor
        }

        // Associação inválida com supervisor de outra empresa
        Representante novoRepresentanteA = Representante.build(supervisor:Supervisor.build())
        empresaA.addUsuario(novoRepresentanteA)
        supervisorEmpresaB.addRepresentante(novoRepresentanteA)
        assertTrue !empresaB.representantes.contains(novoRepresentanteA)
        assertTrue !supervisorEmpresaB.subordinados.contains(novoRepresentanteA)

        // Mudando o representante de empresa
        empresaB.addUsuario(novoRepresentanteA)
        supervisorEmpresaB.addRepresentante(novoRepresentanteA)
        assertTrue empresaB.representantes.contains(novoRepresentanteA)
        assertTrue supervisorEmpresaB.subordinados.contains(novoRepresentanteA)
    }

    void testSaveTelefoneInvalido() {
        assertNull EmpresaFixture.crieTelefoneInvalido().save(flush:true)
    }

    void testSaveEnderecoInvalido() {
        Endereco enderecoInvalidoSemLogradouro = EmpresaFixture.crieEnderecoInvalidoSemLogradouro()
        assertNull enderecoInvalidoSemLogradouro.save(flush:true)

        Endereco enderecoInvalidoSemBairro = EmpresaFixture.crieEnderecoInvalidoSemBairro()
        assertNull enderecoInvalidoSemBairro.save(flush:true)
    }

}