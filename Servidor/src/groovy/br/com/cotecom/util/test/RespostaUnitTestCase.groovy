package br.com.cotecom.util.test

import br.com.cotecom.domain.cotacao.Cotacao
import br.com.cotecom.domain.usuarios.Usuario
import br.com.cotecom.domain.usuarios.empresa.Empresa
import br.com.cotecom.util.fixtures.CotacaoFixture
import br.com.cotecom.util.fixtures.EmpresaFixture
import br.com.cotecom.util.fixtures.UsuarioFixture
import grails.test.GrailsUnitTestCase
import br.com.cotecom.services.DataService
import br.com.cotecom.services.CotacaoService

public abstract class RespostaUnitTestCase extends GrailsUnitTestCase{

    Empresa cliente
    Empresa fornecedor1
    Empresa fornecedor2
    Usuario comprador
    Usuario representante1
    Usuario representante2
    Cotacao cotacao

    DataService dataService
    CotacaoService cotacaoService

    void setUp(){
        super.setUp()
        cliente = EmpresaFixture.crieClienteCompletoComEnderecoETelefone().save(flush:true)
        fornecedor1 = EmpresaFixture.crieFornecedorCompletoComEnderecoETelefone().save(flush:true)
        fornecedor2 = EmpresaFixture.crieFornecedorCompletoComEnderecoETelefone().save(flush:true)

        comprador = UsuarioFixture.crieComprador()
        comprador.empresa = cliente
        comprador.save(flush:true)

        representante1 = UsuarioFixture.crieRepresentanteSemEmpresa()
        representante1.empresa = fornecedor1
        representante1.save(flush:true)
        representante2 = UsuarioFixture.crieRepresentanteSemEmpresa()
        representante2.empresa = fornecedor2
        representante2.save(flush:true)

        cotacao = CotacaoFixture.crieCotacao(comprador)
        cotacao.envie([representante1, representante2])
        cotacao = Cotacao.get(cotacao.id)
    }
}