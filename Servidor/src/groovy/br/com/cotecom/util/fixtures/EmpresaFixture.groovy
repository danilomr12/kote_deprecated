package br.com.cotecom.util.fixtures

import cr.co.arquetipos.password.PasswordTools
import br.com.cotecom.domain.usuarios.empresa.*

public class EmpresaFixture {
                                            

    static def crieFornnecedor(){
        Fornecedor empresa = new Fornecedor(email: "${PasswordTools.saltPasswordBase64("abc")}@terraatacado.com",
			nomeFantasia: "Lider Atacadista", razaoSocial: "Terra Atacado Distribuidor LTDA")
        empresa.addTelefone crieTelefone()
        empresa.endereco = crieEndereco()
        return empresa
    }

    static def crieClienteComEnderecoETelefone(){
        Cliente empresa = crieClienteCompleto()
        empresa.addTelefone crieTelefoneResidencial()
        empresa.endereco = crieEnderecoLongo()
        return empresa
    }

    static Cliente crieClienteCompleto() {
        return new Cliente(nomeFantasia: "Supermercado Joinha", email: crieEmailSimples(), ramo: crieRamoSupermercado(),
            razaoSocial: "Supermercado joinha comercio de secos e molhados LTDA", cnpj: "03.847.655/0001-98")
    }

    static def crieFornecedorMegafort() {
 		return new Fornecedor(email: "${PasswordTools.saltPasswordBase64("abc")}@megafort.com", nomeFantasia: "Megafort",
             razaoSocial: "MegaFort distribuidora imp. e exp. LTDA", ramo:new Ramo(tipo:Ramo.SUPERMERCADO))
    }

	static def crieTelefone() {
		return new Telefone(ddd: "123", tipo: Telefone.FAX, numero: "98754546")
	}

	static def crieEndereco() {
		return new Endereco(logradouro: "rua s", cidade: "asdsa", estado: "go", numero: 3)
	}

    static Fornecedor crieFornecedorCompleto() {
        return new Fornecedor(nomeFantasia: "Megafort", email: crieEmailSimples(), ramo: crieRamoSupermercado(),
            razaoSocial: "MegaFort distribuidora importação e exportação LTDA", cnpj: "38.699.062/0001-06")
    }

    static Fornecedor crieFornecedorCompletoComEnderecoETelefone() {
        Fornecedor fornecedor = crieFornecedorCompleto()
        //fornecedor.addTelefone(crieTelefoneResidencial())
        fornecedor.endereco = crieEnderecoLongo()
        return fornecedor
    }

    static Cliente crieClienteCompletoComEnderecoETelefone() {
        Cliente cliente = new Cliente(nomeFantasia: "Bom Brasil", email: crieEmailSimples(), ramo: crieRamoSupermercado(),
            razaoSocial: "Bom Brasil Supermercados S/A", cnpj: "10.731.655/0001-98")
        cliente.addTelefone crieTelefoneResidencial()
        cliente.endereco = crieEnderecoLongo()
        return cliente
    }

    static Fornecedor crieFornecedorSimples() {
        return new Fornecedor(nomeFantasia: "Megafort", email: crieEmailSimples(), ramo: crieRamoSupermercado())
    }

    static Cliente crieClienteSimples() {
        return new Cliente(nomeFantasia: "Marcos", email: crieEmailSimples(), ramo: crieRamoSupermercado())
    }


    static Ramo crieRamoSupermercado() {
        Ramo ramo = new Ramo(tipo: Ramo.SUPERMERCADO)
        return ramo
    }

    static String crieEmailIncorreto() {
        return "${PasswordTools.saltPasswordBase64("abc")}"
    }

    static String crieEmailSimples() {
        return "${PasswordTools.saltPasswordBase64("abc")}@empresa.com"
    }

    static Telefone crieTelefoneInvalido() {
        return new Telefone(ddd: "61", numero: "")
    }

    static Telefone crieTelefoneSemTipo() {
        return new Telefone(ddd: "62", numero: "4444-4444")
    }

    static Telefone crieTelefoneFax() {
        return new Telefone(ddd: "061", tipo: Telefone.FAX, numero: "3333-3333")
    }

    static Telefone crieTelefoneCelular() {
        return new Telefone(ddd: "011", tipo: Telefone.CELULAR, numero: "2222-2222")
    }

    static Telefone crieTelefoneResidencial() {
        return new Telefone(ddd: "062", tipo: Telefone.RESIDENCIAL, numero: "1111-1111")
    }

    static Endereco crieEnderecoInvalidoSemCep() {
        return new Endereco(logradouro: "Alameda Buritis",
            numero: "999", cidade: "Goiânia", estado: "GO", bairro: "Setor Central")
    }

    static Endereco crieEnderecoInvalidoSemBairro() {
        return new Endereco(logradouro: "Alameda Buritis", numero: "999", cidade: "Goiânia",
            estado: "GO", cep: "74015-080")
    }

    static Endereco crieEnderecoInvalidoSemLogradouro() {
        return new Endereco(numero: "999", cidade: "Goiânia", estado: "GO", bairro: "Setor Central", cep: "74015-080")
    }

    static Endereco crieEnderecoLongo() {
        return new Endereco(logradouro: "Alameda Buritis", numero: "999", cidade: "Goiânia", estado: "GO",
            bairro: "Setor Central", cep: "74015-080")
    }

    static Endereco crieEnderecoCurto() {
        return new Endereco(logradouro: "Av. Tocantins", numero: "999", complemento: "Em frente a casa da mãe Joana",
            cidade: "Goiânia", estado: "GO", bairro: "Setor Central", cep: "74015-080")
    }

}