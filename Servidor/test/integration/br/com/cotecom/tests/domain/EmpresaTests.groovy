package br.com.cotecom.tests.domain

import br.com.cotecom.domain.usuarios.Comprador
import br.com.cotecom.domain.usuarios.Representante
import br.com.cotecom.domain.usuarios.Supervisor
import br.com.cotecom.util.fixtures.EmpresaFixture
import br.com.cotecom.util.fixtures.UsuarioFixture
import grails.test.GrailsUnitTestCase
import br.com.cotecom.domain.usuarios.empresa.*

public class EmpresaTests extends GrailsUnitTestCase{

    void setUp(){
        super.setUp()
    }

    void tearDown(){
        super.tearDown()
    }

    void testSalveEmpresa(){
        // Teste usar� o fornecedor como um exemplo de empresa mas testaremos apenas o comportamento de empresa

        Endereco enderecoCurto = EmpresaFixture.crieEnderecoCurto()
        Endereco enderecoLongo = EmpresaFixture.crieEnderecoLongo()
        Endereco enderecoInvalidoSemLogradouro = EmpresaFixture.crieEnderecoInvalidoSemLogradouro()

        Telefone telefoneResidencial = EmpresaFixture.crieTelefoneResidencial()
        Telefone telefoneCelular = EmpresaFixture.crieTelefoneCelular()
        Telefone telefoneFax = EmpresaFixture.crieTelefoneFax()
        Telefone telefoneSemTipo = EmpresaFixture.crieTelefoneSemTipo()
        Telefone telefoneInvalido = EmpresaFixture.crieTelefoneInvalido()

        // Existem três tipos de empresa
        Fornecedor fornecedor = EmpresaFixture.crieFornecedorSimples()
        Fornecedor fornecedorCompleto = EmpresaFixture.crieFornecedorCompleto()

        fornecedor.endereco = enderecoCurto
        fornecedor.addTelefone telefoneCelular
        fornecedor.addTelefone telefoneFax
        fornecedor.addTelefone telefoneResidencial
        fornecedor.addTelefone telefoneSemTipo
        fornecedor.save(flush:true)

        assertTrue fornecedor.telefones.size() == 4
        assertNotNull fornecedor.endereco
        assertEquals fornecedor.nomeFantasia, "Megafort"
        assertNull fornecedor.razaoSocial
        assertNull fornecedor.cnpj
        // assertEquals fornecedor.email.email, "empresa@empresa.com"  Email gerado dinamicamente n�o pode ser validado
        assertEquals 1, fornecedor.telefones.findAll{Telefone telefone -> telefone.numero == "1111-1111"}.size()
        assertEquals 1, fornecedor.telefones.findAll{Telefone telefone -> telefone.numero == "2222-2222"}.size()
        assertEquals 1, fornecedor.telefones.findAll{Telefone telefone -> telefone.numero == "3333-3333"}.size()
        assertEquals 1, fornecedor.telefones.findAll{Telefone telefone -> telefone.numero == "4444-4444"}.size()
        assertEquals fornecedor.endereco.logradouro, "Av. Tocantins"


        // Garantir que devemos ter pelo menos um endereco e pelo menos um telefone para salvar
        fornecedorCompleto.endereco = enderecoCurto
        fornecedorCompleto.addTelefone telefoneResidencial
        assertNotNull fornecedorCompleto.save(flush:true)
        assertTrue fornecedorCompleto.telefones.size() == 1
        assertNotNull fornecedorCompleto.endereco
        assertTrue fornecedorCompleto.telefones.contains(telefoneResidencial) && fornecedorCompleto.endereco.equals(enderecoCurto)

        // Adicionando informa��es inv�lidas
        fornecedor.addTelefone telefoneInvalido
        fornecedor.save(flush:true)
        assertTrue !fornecedor.telefones.contains(telefoneInvalido)

        fornecedorCompleto.addEndereco enderecoInvalidoSemLogradouro
        fornecedorCompleto.save(flush:true)
        assertEquals fornecedorCompleto.endereco, enderecoInvalidoSemLogradouro
    }

    void testSalveFornecedor(){
        Fornecedor fornecedorCompleto = EmpresaFixture.crieFornecedorCompleto()
        fornecedorCompleto.save(flush:true)

        List<Supervisor> supervisores = []
        List<Representante> representantes = []
        10.times {
            supervisores << UsuarioFixture.crieSupervisorSemEmpresa().save(flush:true)
            representantes << Representante.build()
        }

        supervisores.each { Supervisor supervisor ->
            fornecedorCompleto.addUsuario supervisor
        }
        representantes.each { Representante representante ->
            fornecedorCompleto.addUsuario representante
        }

        fornecedorCompleto.supervisores.eachWithIndex { Supervisor supervisor, int index ->
            assertTrue supervisores.contains(supervisor)
            assertEquals fornecedorCompleto, supervisor.empresa
        }

        fornecedorCompleto.representantes.eachWithIndex { Representante representante, int index ->
            assertTrue representantes.contains(representante)
            assertEquals fornecedorCompleto, representante.empresa
        }
    }

    void testSalveCliente(){
        Cliente clienteCompleto = EmpresaFixture.crieClienteCompleto()
        clienteCompleto.save(flush:true)

        List<Comprador> compradores = []
        10.times {
            compradores << Comprador.build()
        }

        compradores.each { clienteCompleto.addUsuario it }

        clienteCompleto.usuarios.each { Comprador comprador ->
            assertTrue compradores.contains(comprador)
            assertEquals clienteCompleto, comprador.empresa
        }
    }

    void testAtendimentos(){
        Representante representante = Representante.build()
        Fornecedor fornecedor = EmpresaFixture.crieFornecedorCompletoComEnderecoETelefone()
        fornecedor.save(flush:true)
        Cliente cliente = EmpresaFixture.crieClienteCompletoComEnderecoETelefone()
        cliente.save(flush:true)

        fornecedor.addUsuario(representante)

        Atendimento atendimento = new Atendimento(representante: representante, fornecedor: fornecedor, cliente:cliente).save(flush:true)
        assertNotNull atendimento.id

        assertEquals representante, atendimento.representante
        assertEquals fornecedor, atendimento.fornecedor
        assertEquals cliente, atendimento.cliente

        assertNotNull Atendimento.findByRepresentanteAndCliente(representante, cliente)
        assertNotNull Atendimento.findByFornecedorAndCliente(fornecedor, cliente)
    }

    void testAtendimentoInvalidoComEmpresaRepresentanteDiferenteFornecedor(){
        Representante representante = Representante.build()
        Fornecedor fornecedor = EmpresaFixture.crieFornecedorCompletoComEnderecoETelefone().save(flush:true)
        Fornecedor fornecedorDiferente = EmpresaFixture.crieFornecedorCompletoComEnderecoETelefone().save(flush:true)
        Cliente cliente = EmpresaFixture.crieClienteCompletoComEnderecoETelefone().save(flush:true)

        fornecedorDiferente.addUsuario(representante)
        
        assertNull new Atendimento(representante:representante, fornecedor:fornecedor, cliente:cliente).save(flush:true)
    }

    void testAtendimentoSemRepresentante(){
        Fornecedor fornecedor = EmpresaFixture.crieFornecedorCompletoComEnderecoETelefone().save(flush:true)
        Cliente cliente = EmpresaFixture.crieClienteCompletoComEnderecoETelefone().save(flush:true)
        assertNotNull new Atendimento(cliente:cliente, fornecedor:fornecedor).save(flush:true)
        assertNull new Atendimento(fornecedor:fornecedor).save(flush:true)
    }

    void testAtendimentoSemFornecedor(){
        Representante representante = Representante.build()
        Cliente cliente = EmpresaFixture.crieClienteCompletoComEnderecoETelefone().save(flush:true)
        assertNotNull new Atendimento(cliente:cliente, representante:representante).save(flush:true)
        assertNull new Atendimento(representante:representante).save(flush:true)
    }

    void testAtendimentoSemFornecedorERepresentanten(){
        Cliente cliente = EmpresaFixture.crieClienteCompletoComEnderecoETelefone().save(flush:true)
        assertNull new Atendimento(cliente:cliente).save(flush:true)
    }
}