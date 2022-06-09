package br.com.cotecom.tests.services

import br.com.cotecom.domain.usuarios.Comprador
import br.com.cotecom.domain.usuarios.Representante
import br.com.cotecom.domain.usuarios.empresa.Atendimento
import br.com.cotecom.domain.usuarios.empresa.Cliente
import br.com.cotecom.domain.usuarios.empresa.Empresa
import br.com.cotecom.domain.usuarios.empresa.Fornecedor
import br.com.cotecom.services.UsuarioService
import br.com.cotecom.util.fixtures.EmpresaFixture
import br.com.cotecom.util.fixtures.UsuarioFixture
import grails.test.GrailsUnitTestCase
import org.hibernate.SessionFactory

public class UsuarioServiceTests extends GrailsUnitTestCase {

    UsuarioService usuarioService
    SessionFactory sessionFactory
    Cliente cliente
    Comprador comprador

    void setUp(){
        super.setUp()
        cliente = crieEmpresaCliente()
        comprador = crieCompradorEAdicioneAoCliente(cliente)
    }

    /*void testGetRepresentantes(){
        Representante representante2
        Representante representante1

        Fornecedor fornecedor1 = EmpresaFixture.crieFornecedorCompletoComEnderecoETelefone()
        fornecedor1.save(flush:true)

        Fornecedor fornecedor2 = EmpresaFixture.crieFornecedorCompletoComEnderecoETelefone()
        fornecedor2.save(flush:true)

        representante1 = new Representante(nome:"rep1", email: EmpresaFixture.crieEmailSimples(), password:"654321")
        fornecedor1.addUsuario(representante1)
        representante1.save(flush:true)

        representante2 = new Representante(nome:"rep2", email: EmpresaFixture.crieEmailSimples(), password:"123456")
        fornecedor2.addUsuario(representante2)
        representante2.save(flush:true)

        Atendimento atendimento1 = new Atendimento(cliente: cliente, fornecedor: fornecedor1, representante: representante1)
        atendimento1.save(flush:true)
        Atendimento atendimento2 = new Atendimento(cliente: cliente, fornecedor: fornecedor2, representante: representante2)
        atendimento2.save(flush:true)

        List<Representante> representantes = usuarioService.getRepresentantesByComprador(comprador)
        assertNotNull representantes

        assertTrue representantes.any { Representante rep -> representante1.equals(rep)}
        assertTrue representantes.any { Representante rep -> representante2.equals(rep)}

        representantes.each {Representante representante ->
            assertNotNull representante.empresa as Fornecedor
            assertNotNull representante.nome
        }

    }

    void testBusqueRepresentante(){
        List<Representante> representantes = UsuarioFixture.crieTresRepresentantes()
        representantes.eachWithIndex {Representante representante, int i ->
            if( i == 0 ){
                representante.nome = "Alberto"
                representante.email = "alberto@cotecom.com.br"
            }
            else if( i == 1 ){
                representante.nome = "Iure"
                representante.email = "iure@cotecom.com.br"
            }
            else if( i == 2 ){
                representante.nome = "Ricardo"
                representante.email = "ricardo@cotecom.com.br"
            }
            representante.save(flush:true)
            def stop
        }

        simulateNewRequest()

        representantes.eachWithIndex {Representante representante, int i ->
            Empresa empresa = representante.empresa
            empresa.email = "${i}vendas@cotecom.com.br"
            empresa.nomeFantasia = "Kote"
            empresa.razaoSocial = "Kote Casa da Luz Vermelha"
            empresa.save(flush:true)
        }

        simulateNewRequest()

        assertEquals 3, usuarioService.busqueNovosRepresentantes(comprador, "vendas@cotecom.com.br").size()
        assertEquals 3, usuarioService.busqueNovosRepresentantes(comprador, "Kote").size()
        assertEquals 3, usuarioService.busqueNovosRepresentantes(comprador, "Luz VERMELHA").size()
        assertNull usuarioService.busqueNovosRepresentantes(comprador, "Nãoexiste")

        assertEquals 1, usuarioService.busqueNovosRepresentantes(comprador, "Alberto").size()
        assertEquals 1, usuarioService.busqueNovosRepresentantes(comprador, "alberto@cotecom.com.br").size()

        assertEquals 1, usuarioService.busqueNovosRepresentantes(comprador, "Ricardo").size()
        assertEquals 1, usuarioService.busqueNovosRepresentantes(comprador, "ricardo@cotecom.com.br").size()

        assertEquals 1, usuarioService.busqueNovosRepresentantes(comprador, "Iure").size()
        assertEquals 1, usuarioService.busqueNovosRepresentantes(comprador, "iure@cotecom.com.br").size()

        assertNull usuarioService.busqueNovosRepresentantes(comprador, "Danilo")
        assertNull usuarioService.busqueNovosRepresentantes(comprador, "danilo@cotecom.com.br")

        assertEquals 3, usuarioService.busqueNovosRepresentantes(comprador, "").size()

        usuarioService.crieAtendimento(representantes.get(0),comprador)
        assertEquals 0, usuarioService.busqueNovosRepresentantes(comprador, "Alberto").size()
    }

    void testSalveRepresentante(){
        assertNull usuarioService.salveRepresentante(null)

        Representante transienteRepresentante = UsuarioFixture.crieRepresentanteSemEmpresa()
        def persistedRepresentante = usuarioService.salveRepresentante(transienteRepresentante)
        assertNotNull transienteRepresentante.id

        persistedRepresentante.nome = "Alberto Barcelos"
        persistedRepresentante.email = "albertoprb@gmail.com"
        usuarioService.salveRepresentante(persistedRepresentante)

        assertEquals "Alberto Barcelos", Representante.get(persistedRepresentante.id).nome
        assertEquals "albertoprb@gmail.com", Representante.get(persistedRepresentante.id).email
    }

    void testSalveEmpresa(){
        assertNull usuarioService.salveEmpresa(null)

        Empresa transienteEmpresa = EmpresaFixture.crieFornecedorMegafort()
        def persistedEmpresa = usuarioService.salveEmpresa(transienteEmpresa)
        assertNotNull transienteEmpresa.id

        persistedEmpresa.nomeFantasia = "Kote"
        persistedEmpresa.email = "cotecom@cotecom.com.br"
        usuarioService.salveEmpresa(persistedEmpresa)

        assertEquals "Kote", Fornecedor.get(persistedEmpresa.id).nomeFantasia
        assertEquals "cotecom@cotecom.com.br", Fornecedor.get(persistedEmpresa.id).email
    }

    void testRemovaFornecedor(){
        Empresa empresa = EmpresaFixture.crieFornecedorMegafort().save(flush:true)
        usuarioService.deleteFornecedor(empresa.id)
        assertNull Fornecedor.get(empresa.id)
    }

    void testRemovaRepresentante(){
        Representante representante = UsuarioFixture.crieRepresentanteSemEmpresa().save(flush:true)
        usuarioService.deleteRepresentante(representante.id)
        assertNull Representante.get(representante.id)
    }

    void testCrieAtendimento(){
        Representante representante = new Representante()

        def atendimento = usuarioService.crieAtendimento(representante, comprador)
        assertNull atendimento

        representante = new Representante(nome:"João Batista de loren ipsun")

        assertNull usuarioService.crieAtendimento(representante, comprador)

        representante = new Representante(nome:"João Batista de loren ipsun", email: "joaolorem@gmail.com")

        assertTrue usuarioService.crieAtendimento(representante, comprador).representante instanceof Representante
    }

    private Comprador crieCompradorEAdicioneAoCliente(Cliente cliente) {
        Comprador comprador = UsuarioFixture.crieComprador()
        comprador.empresa = cliente
        comprador.save(flush: true)
        return comprador
    }

    private Cliente crieEmpresaCliente() {
        Cliente cliente = EmpresaFixture.crieClienteComEnderecoETelefone()
        cliente.save(flush:true)
        return cliente
    }

    private simulateNewRequest(){
        sessionFactory.getCurrentSession().flush()
        sessionFactory.getCurrentSession().clear()
    }

*/
}