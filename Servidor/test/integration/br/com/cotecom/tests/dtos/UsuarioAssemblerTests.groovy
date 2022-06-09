package br.com.cotecom.tests.dtos

import br.com.cotecom.domain.usuarios.Comprador
import br.com.cotecom.domain.usuarios.Supervisor
import br.com.cotecom.dtos.assembler.UsuarioAssembler
import br.com.cotecom.util.test.UsuarioUnitTestCase

public class UsuarioAssemblerTests extends UsuarioUnitTestCase{

    void setUp(){
        super.setUp()
    }

    void tearDown(){
        super.tearDown()
    }

    /*void testUsuarioAssemblerForRepresentante(){
        RepresentanteDTO representanteDTO = UsuarioAssembler.crieUsuarioDTO(representante)
        assertEquals representanteDTO.nome, representante.nome
        assertEquals representanteDTO.empresa.nomeFantasia, representante.empresa?.nomeFantasia
        assertEquals representanteDTO.id, representante.id
        assertEquals representanteDTO.telefones.size(), representante.telefones.size()

        representanteDTO.telefones = new ArrayList()
        def novoRepresentante = UsuarioAssembler.crieRepresentante(representanteDTO)
        assertEquals representanteDTO.nome, novoRepresentante.nome
        assertEquals representanteDTO.empresa.nomeFantasia, novorepresentante.empresa?.nomeFantasia
        assertEquals representanteDTO.id, novoRepresentante.id
        assertEquals representanteDTO.telefones.size(), novoRepresentante.telefones.size()

        RepresentanteDTO representanteNullDTO = UsuarioAssembler.crieRepresentanteDTO(representanteNull)
        assertNull representanteNullDTO
        representanteNull = UsuarioAssembler.crieRepresentante(representanteNullDTO)
        assertNull representanteNull

        RepresentanteDTO representanteInvalidoDTO = UsuarioAssembler.crieRepresentanteDTO(representanteInvalido)
        assertEquals representanteInvalidoDTO.nome, representanteInvalido.nome
        assertEquals representanteInvalidoDTO.id, representanteInvalido.id

        representanteInvalido = UsuarioAssembler.crieRepresentante(representanteInvalidoDTO)
        assertEquals representanteInvalidoDTO.nome, representanteInvalido.nome
        assertEquals representanteInvalidoDTO.id, representanteInvalido.id

    }
    
    void testUsuarioAssemblerForComprador(){
        CompradorDTO compradorDTO = UsuarioAssembler.crieCompradorDTO(comprador)
        assertEquals compradorDTO.nome, comprador.nome
        assertEquals compradorDTO.empresa.nomeFantasia, comprador.empresa?.nomeFantasia
        assertEquals compradorDTO.id, comprador.id
        assertEquals compradorDTO.telefones.size(), comprador.telefones.size()

        compradorDTO.telefones = new ArrayList()
        Comprador novoComprador = UsuarioAssembler.crieComprador(compradorDTO)
        assertEquals compradorDTO.nome, novoComprador.nome
        assertEquals compradorDTO.empresa.nomeFantasia, novoComprador.empresa?.nomeFantasia
        assertEquals compradorDTO.id, novoComprador.id
        assertEquals compradorDTO.telefones.size(), novoComprador.telefones.size()

        CompradorDTO compradorNullDTO = UsuarioAssembler.crieCompradorDTO(compradorNull)
        assertNull compradorNullDTO
        compradorNull = UsuarioAssembler.crieComprador(compradorNullDTO)
        assertNull compradorNull

        CompradorDTO compradorInvalidoDTO = UsuarioAssembler.crieCompradorDTO(compradorInvalido)
        assertEquals compradorInvalidoDTO.nome, compradorInvalido.nome
        assertEquals compradorInvalidoDTO.id, compradorInvalido.id

        compradorInvalido = UsuarioAssembler.crieComprador(compradorInvalidoDTO)
        assertEquals compradorInvalidoDTO.nome, compradorInvalido.nome
        assertEquals compradorInvalidoDTO.id, compradorInvalido.id

    }

    void testUsuarioAssemblerForSupervisor(){
        SupervisorDTO supervisorDTO = UsuarioAssembler.crieSupervisorDTO(supervisor)
        assertEquals supervisorDTO.nome, supervisor.nome
        assertEquals supervisorDTO.empresa.nomeFantasia, supervisor.empresa?.nomeFantasia
        assertEquals supervisorDTO.id, supervisor.id
        assertEquals supervisorDTO.telefones.size(), supervisor.telefones.size()

        supervisorDTO.telefones = new ArrayList()
        Supervisor novoSupervisor = UsuarioAssembler.crieSupervisor(supervisorDTO)
        assertEquals supervisorDTO.nome, novoSupervisor.nome
        assertEquals supervisorDTO.empresa.nomeFantasia, novoSupervisor.empresa?.nomeFantasia
        assertEquals supervisorDTO.id, novoSupervisor.id
        assertEquals supervisorDTO.telefones.size(), novoSupervisor.telefones.size()

        SupervisorDTO supervisorNullDTO = UsuarioAssembler.crieSupervisorDTO(supervisorNull)
        assertNull supervisorNullDTO
        supervisorNull = UsuarioAssembler.crieSupervisor(supervisorNullDTO)
        assertNull supervisorNull

        SupervisorDTO supervisorInvalidoDTO = UsuarioAssembler.crieSupervisorDTO(supervisorInvalido)
        assertEquals supervisorInvalidoDTO.nome, supervisorInvalido.nome
        assertEquals supervisorInvalidoDTO.id, supervisorInvalido.id

        supervisorInvalido = UsuarioAssembler.crieSupervisor(supervisorInvalidoDTO)
        assertEquals supervisorInvalidoDTO.nome, supervisorInvalido.nome
        assertEquals supervisorInvalidoDTO.id, supervisorInvalido.id
    }*/

}