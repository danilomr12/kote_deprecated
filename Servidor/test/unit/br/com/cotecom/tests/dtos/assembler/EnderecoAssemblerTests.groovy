package br.com.cotecom.tests.dtos.assembler

import br.com.cotecom.domain.dto.usuario.EnderecoDTO
import br.com.cotecom.domain.usuarios.empresa.Endereco
import br.com.cotecom.dtos.assembler.EnderecoAssembler
import grails.test.GrailsUnitTestCase

public class EnderecoAssemblerTests extends GrailsUnitTestCase{

    void testCrieEnderecoDTOECrieEndereco(){
        Endereco endereco = new Endereco()
        EnderecoDTO enderecoDTO

        assertNull EnderecoAssembler.crieEndereco(null)
        assertNull EnderecoAssembler.crieEnderecoDTO(null)
        assertEquals EnderecoAssembler.crieEnderecos(null).size(), 0
        assertEquals EnderecoAssembler.crieEnderecosDTO(null).size(), 0

        def result = EnderecoAssembler.crieEnderecosDTO([null,null] as HashSet)
        assertEquals result.size(), 1
        result.each {
          assertTrue it == null
        }
        result = EnderecoAssembler.crieEnderecos([null,null] as ArrayList)
        assertEquals result.size(), 1
        result.each {
          assertTrue it == null
        }
        assertTrue EnderecoAssembler.crieEnderecoDTO(endereco) instanceof EnderecoDTO

        endereco.bairro = "bairro"
        endereco.cep = "74123512"
        endereco.cidade = "cidade"
        endereco.complemento = "complemento"
        endereco. estado = "estado"
        endereco.logradouro = "logradouro"
        endereco.numero = "numero"

        enderecoDTO = EnderecoAssembler.crieEnderecoDTO(endereco)

        assertEquals enderecoDTO.bairro, endereco.bairro
        assertEquals enderecoDTO.cep, endereco.cep
        assertEquals enderecoDTO.cidade, endereco.cidade
        assertEquals enderecoDTO.complemento, endereco.complemento
        assertEquals enderecoDTO.estado, endereco.estado
        assertEquals enderecoDTO.logradouro, endereco.logradouro
        assertEquals enderecoDTO.numero, endereco.numero
    }
}