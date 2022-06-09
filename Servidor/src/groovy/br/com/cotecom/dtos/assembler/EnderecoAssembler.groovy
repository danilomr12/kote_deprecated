package br.com.cotecom.dtos.assembler

import br.com.cotecom.domain.dto.usuario.EnderecoDTO
import br.com.cotecom.domain.usuarios.empresa.Endereco

public class EnderecoAssembler {

    static EnderecoDTO crieEnderecoDTO(Endereco endereco) {
        if (endereco == null) return null
        return new EnderecoDTO(id: endereco?.id, logradouro: endereco?.logradouro, cidade: endereco?.cidade,
                estado: endereco?.estado, numero: endereco?.numero, complemento: endereco?.complemento,
                bairro: endereco?.bairro, cep: endereco?.cep, version: endereco?.version)
    }

    static def crieEndereco(EnderecoDTO enderecoDTO) {
        if (enderecoDTO == null || isBlank(enderecoDTO)) return null
        if (enderecoDTO.id == null)
            return new Endereco(logradouro: enderecoDTO.logradouro, cidade: enderecoDTO.cidade,
                    estado: enderecoDTO.estado, numero: enderecoDTO.numero, cep: enderecoDTO.cep,
                    complemento: enderecoDTO.complemento, bairro: enderecoDTO.bairro)

        Endereco endereco = Endereco.get(enderecoDTO.id)
        endereco.logradouro = enderecoDTO.logradouro
        endereco.cidade = enderecoDTO.cidade
        endereco.estado = enderecoDTO.estado
        endereco.complemento = enderecoDTO.complemento
        endereco.numero = enderecoDTO.numero
        endereco.bairro = enderecoDTO.bairro
        endereco.cep = enderecoDTO.cep
        return endereco
    }

    static Boolean isBlank(EnderecoDTO enderecoDTO) {
        return (enderecoDTO.bairro != null ? enderecoDTO.bairro?.isEmpty() : true) &&
                (enderecoDTO.cep != null ? enderecoDTO.cep?.isEmpty() : true) &&
                (enderecoDTO.cidade != null ? enderecoDTO.cidade.isEmpty() :true) &&
                (enderecoDTO.complemento != null ? enderecoDTO.complemento?.isEmpty() : true) &&
                (enderecoDTO.estado != null ? enderecoDTO.estado?.isEmpty() : true) &&
                (enderecoDTO.logradouro != null ? enderecoDTO.logradouro?.isEmpty() : true) &&
                (enderecoDTO.numero != null ? enderecoDTO.numero?.isEmpty() : true)
    }

    static Set<Endereco> crieEnderecos(List<EnderecoDTO> enderecoDTOs) {
        return enderecoDTOs.collect { crieEndereco(it) } as Set
    }

    static List<EnderecoDTO> crieEnderecosDTO(Set<Endereco> enderecos) {
        List enderecosResutl = enderecos.collect { crieEnderecoDTO(it) }
        return enderecosResutl
    }
}