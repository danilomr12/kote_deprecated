package br.com.cotecom.dtos.assembler

import br.com.cotecom.domain.dto.usuario.TelefoneDTO
import br.com.cotecom.domain.usuarios.empresa.Telefone

public class TelefoneAssembler {

    static List<TelefoneDTO> crieTelefonesDTO(Set<Telefone> telefones) {
        List<TelefoneDTO>telefoneDTOs = new ArrayList<TelefoneDTO>()
        telefones?.each {Telefone it ->
            telefoneDTOs.push(crieTelefoneDTO(it))
        }
        return telefoneDTOs
    }

    static TelefoneDTO crieTelefoneDTO(Telefone telefone) {
        return new TelefoneDTO(id:telefone.id, numero: telefone.numero, tipo:telefone.tipo, ddd:telefone.ddd)
    }

    static Telefone crieTelefone(TelefoneDTO telefoneDTO) {
        Telefone fone = new Telefone()
        if(telefoneDTO == null) return null
        if(telefoneDTO.id)
            fone = Telefone.get(telefoneDTO.id)            
        fone.ddd = telefoneDTO.ddd
        fone.numero = telefoneDTO.numero
        fone.tipo = telefoneDTO.tipo
        return fone
    }

    static List<Telefone> crieTelefones(List<TelefoneDTO> telefoneDTOs) {
        List<Telefone> telefones = new ArrayList()
        telefoneDTOs.each {TelefoneDTO dto ->
            telefones.add(crieTelefone(dto))
        }
        return telefones
    }
}