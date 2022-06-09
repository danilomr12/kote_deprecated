package br.com.cotecom.tests.services.remote

import br.com.cotecom.domain.usuarios.Comprador
import br.com.cotecom.domain.usuarios.Representante
import br.com.cotecom.domain.usuarios.Supervisor
import br.com.cotecom.domain.usuarios.empresa.Telefone
import br.com.cotecom.dtos.assembler.UsuarioAssembler
import br.com.cotecom.services.UsuarioService
import br.com.cotecom.services.remote.RemoteUsuarioService
import br.com.cotecom.util.fixtures.UsuarioFixture
import grails.test.GrailsUnitTestCase

public class RemoteUsuarioServiceTests extends GrailsUnitTestCase {

    RemoteUsuarioService remoteUsuarioService
    UsuarioService usuarioService

    /*void testSaveComprador(){
        Comprador comprador = UsuarioFixture.crieCompradorComEmpresa().save(flush:true)
        CompradorDTO compradorDTO = UsuarioAssembler.crieCompradorDTO(comprador)
        compradorDTO = simulaEdicao(compradorDTO)
        
        assertTrue 'Falha ao atualizar comprador', remoteUsuarioService.saveComprador(compradorDTO)

        Comprador compradorAtualizado = Comprador.get(comprador.id)
        assertEquals compradorDTO.email , compradorAtualizado.email
        assertEquals compradorDTO.nome , compradorAtualizado.nome
        assertEquals compradorDTO.telefones[0].ddd , compradorAtualizado.telefones[0].ddd
        assertEquals compradorDTO.telefones[0].numero , compradorAtualizado.telefones[0].numero
        assertEquals compradorDTO.telefones[0].tipo , compradorAtualizado.telefones[0].tipo                
    }

    def simulaEdicao(CompradorDTO compradorDTO) {
        compradorDTO.email = 'iure@cotecom.com.br'
        compradorDTO.nome = 'Iure Guimar?es'
        compradorDTO.telefones[0].ddd = '62'
        compradorDTO.telefones[0].numero = '9915-6869'
        compradorDTO.telefones[0].tipo = 1
        compradorDTO
    }

    void testSaveRepresentante(){
        RepresentanteDTO representanteDTO = new RepresentanteDTO()
        assertNull remoteUsuarioService.saveRepresentante(representanteDTO)

        representanteDTO = UsuarioAssembler.crieRepresentanteDTO(Representante.build())
        ArrayList<TelefoneDTO> telefoneDTOs = new ArrayList<TelefoneDTO>()
        telefoneDTOs.add(new TelefoneDTO(ddd:"62", numero:"32332354",tipo: Telefone.RESIDENCIAL))
        telefoneDTOs.add(new TelefoneDTO(ddd:"62", numero:"92134213",tipo: Telefone.CELULAR))
        representanteDTO.telefones = new ArrayList()
        telefoneDTOs.each {TelefoneDTO dto ->
            representanteDTO.telefones.push(dto)
        }
        RepresentanteDTO representanteSalvo = remoteUsuarioService.saveRepresentante(representanteDTO)

        assertEquals telefoneDTOs.size(), representanteSalvo.telefones.size()
        telefoneDTOs.each {TelefoneDTO dto ->
            assertTrue contemTelefone(representanteSalvo.telefones, dto)
        }
    }

    void testSaveSupervisor(){
        SupervisorDTO supervisorDTO = UsuarioAssembler.crieSupervisorDTO(Supervisor.build())
        ArrayList<TelefoneDTO> telefoneDTOs = new ArrayList<TelefoneDTO>()
        telefoneDTOs.add(new TelefoneDTO(ddd:"62", numero:"32332354",tipo: Telefone.RESIDENCIAL))
        telefoneDTOs.add(new TelefoneDTO(ddd:"62", numero:"92134213",tipo: Telefone.CELULAR))
        supervisorDTO.telefones = telefoneDTOs

        SupervisorDTO supervisorSalvo = remoteUsuarioService.saveSupervisor(supervisorDTO)

        assertEquals telefoneDTOs.size(), supervisorSalvo.telefones.size()
        telefoneDTOs.each {TelefoneDTO dto ->
            assertTrue contemTelefone(supervisorSalvo.telefones, dto)
        }
    }

    boolean contemTelefone(ArrayList<TelefoneDTO> telefoneDTOs, TelefoneDTO  telefoneDTO){
        def results = new ArrayList()
        telefoneDTOs.each {TelefoneDTO dto->
            if(dto.numero == telefoneDTO.numero && dto.ddd == telefoneDTO.ddd &&
                    dto.tipo == telefoneDTO.tipo){
                results.push(true)
            }else{
                results.push(false)
            }
        }
        if(results.contains(true))
            return true
        return false
    }*/
}