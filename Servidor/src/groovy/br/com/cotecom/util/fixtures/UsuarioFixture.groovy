package br.com.cotecom.util.fixtures

import br.com.cotecom.domain.usuarios.Comprador
import br.com.cotecom.domain.usuarios.Representante
import br.com.cotecom.domain.usuarios.Supervisor
import br.com.cotecom.domain.usuarios.empresa.Cliente
import br.com.cotecom.domain.usuarios.empresa.Fornecedor

public class UsuarioFixture {


    static List<Representante> crieTresRepresentantes() {
        def representantes = new ArrayList<Representante>()
        for(int i=1; i<=3;i++){
            Representante rep = Representante.build(supervisor:Supervisor.build())
            rep.email = "R" + new Random().nextInt(Integer.MAX_VALUE) + "@cotecom.com.br"
            Fornecedor fornecedor1 = EmpresaFixture.crieFornecedorCompletoComEnderecoETelefone()
            fornecedor1.nomeFantasia = "Fornecedor$i"
            fornecedor1.razaoSocial = "Fornecedor$i LTDA"
            fornecedor1.cnpj = "03.8${i+1}47.6${i-1}5/0001-9${i}"
            fornecedor1.save(flush:true)
            fornecedor1.addUsuario rep
            representantes << rep
        }
        return representantes
    }

    static Comprador crieComprador() {
        def comprador = new Comprador(nome:"comprador1", password:"nsn32fs8", email:"r" + new Random().nextInt(Integer.MAX_VALUE) + "@cotecom.com.br")
        comprador.save(flush:true)
        return comprador
    }

    static def crieRepresentantes() {
        Fornecedor empresa1 = new Fornecedor(nomeFantasia:"empresa1", razaoSocial:"empresa1 LTDA").save(flush:true)
        Fornecedor empresa2 = new Fornecedor(nomeFantasia:"empresa2", razaoSocial:"empresa2 LTDA").save(flush:true)
        Representante representante1 = new Representante(nome: "representante1", password: "n13ffs8",
                email: "representante1@cotecom.com.br", empresa: empresa1).save(flush: true)
        Representante representante2 = new Representante(nome: "representante2", password: "n23445g",
                email: "representante2@cotecom.com.br", empresa: empresa2).save(flush: true)
        return [representante1, representante2]
    }

    static Representante crieRepresentanteSemEmpresa(){
        Representante represenatante = new Representante(nome: "Representante1", email: EmpresaFixture.crieEmailSimples(), password: "pass2pass")
        return represenatante
    }

    static Supervisor crieSupervisorSemEmpresa(){
        Supervisor supervisor = new Supervisor(nome: "Danilo", email: EmpresaFixture.crieEmailSimples(), password: "pass2pass")
        supervisor.addTelefone(EmpresaFixture.crieTelefoneCelular())
        supervisor.addTelefone(EmpresaFixture.crieTelefoneResidencial())
        return supervisor
    }

    static Comprador crieCompradorComEmpresa(){
        Comprador comprador = new Comprador(nome: "Danilo", email: EmpresaFixture.crieEmailSimples(), password: "pass2pass")
        comprador.addTelefone(EmpresaFixture.crieTelefoneCelular())
        comprador.addTelefone(EmpresaFixture.crieTelefoneResidencial())
        comprador.save(flush:true)

        Cliente supermercado = EmpresaFixture.crieClienteCompletoComEnderecoETelefone()
        supermercado.save(flush:true)
        
        supermercado.addUsuario comprador

        return comprador
    }

}