package br.com.cotecom.dtos.assembler

import br.com.cotecom.domain.usuarios.Comprador
import br.com.cotecom.domain.usuarios.Representante
import br.com.cotecom.domain.usuarios.Supervisor
import br.com.cotecom.domain.usuarios.empresa.Atendimento
import br.com.cotecom.domain.usuarios.empresa.Telefone
import br.com.cotecom.domain.dto.usuario.*
import br.com.cotecom.domain.usuarios.Usuario
import br.com.cotecom.domain.usuarios.Estoquista
import br.com.cotecom.services.UsuarioService
import br.com.cotecom.domain.usuarios.Responsabilidade

static public class UsuarioAssembler {

    static List<Representante> crieRepresentantesFromIds(List<Long> idsRepresentantes) {
        List<Representante> representantes = new ArrayList()
        idsRepresentantes.each {Long idRepresentante ->
            representantes.push(Representante.get(idRepresentante))
        }
        return representantes
    }

    static List<AtendimentoDTO> crieAtendimentosDTO(List<Atendimento> atendimentos) {
        if(atendimentos == null)
            return null
        List<AtendimentoDTO> atendimentosDTO = new ArrayList()
        atendimentos.each {Atendimento atendimento ->
            atendimentosDTO.add(crieAtendimentoDTO(atendimento))
        }
        return atendimentosDTO
    }

    static AtendimentoDTO crieAtendimentoDTO(Atendimento atendimento) {
        if(atendimento == null)
            return null
        AtendimentoDTO atendimentoDTO = new AtendimentoDTO()
        atendimentoDTO.id = atendimento.id
        atendimentoDTO.clienteId = atendimento.cliente.id
        atendimentoDTO.fornecedorId = atendimento.fornecedor.id
        atendimentoDTO.representanteId = atendimento.representante.id
        return atendimentoDTO
    }

    static UsuarioDTO crieUsuarioDTO(Usuario usuario) {
        if (!usuario) return null
        EmpresaDTO empresaDTO = EmpresaAssembler.crieEmpresaDTO(usuario.empresa)
        UsuarioDTO usuarioDTO = new UsuarioDTO(id: usuario.id, nome: usuario.nome, email: usuario.email,
                empresa: empresaDTO, password: usuario.password)

        if(usuario instanceof Comprador){
            usuarioDTO.tipo = TipoUsuario.COMPRADOR
        } else if(usuario instanceof Estoquista){
            usuarioDTO.tipo = TipoUsuario.ESTOQUISTA
        }else if(usuario instanceof Representante){
            usuarioDTO.tipo = TipoUsuario.REPRESENTANTE
            usuarioDTO.supervisor = crieUsuarioDTO(usuario.supervisor)
        }else if(usuario instanceof Supervisor){
            usuarioDTO.tipo = TipoUsuario.SUPERVISOR
        }
        usuarioDTO.telefones = new ArrayList<TelefoneDTO>()
        TelefoneAssembler.crieTelefonesDTO(usuario.telefones).each {TelefoneDTO telefoneDTO ->
            usuarioDTO.telefones.add(telefoneDTO)
        }
        return usuarioDTO
    }

    static Usuario crieUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuario
        if (!usuarioDTO) return null
        if (!usuarioDTO.id){
            if(usuarioDTO.tipo == TipoUsuario.COMPRADOR){
                usuario = new Comprador()
            }else if(usuarioDTO.tipo == TipoUsuario.ESTOQUISTA){
                usuario = new Estoquista()
            }else if(usuarioDTO.tipo == TipoUsuario.REPRESENTANTE){
                usuario = new Representante()
            }else{
                usuario = new Supervisor(password: "senhaTemp", habilitado: false)
            }
        }else{
            usuario = Usuario.get(usuarioDTO.id)
        }
        if (!usuario) return null

        usuario.nome = usuarioDTO.nome
        usuario.email = usuarioDTO.email
        usuario.empresa = EmpresaAssembler.crieEmpresa(usuarioDTO.empresa)

        TelefoneAssembler.crieTelefones(usuarioDTO.telefones).each {Telefone telefone ->
            Telefone fone = usuario.telefones.find {Telefone it->
                it.id != null && telefone.id != null && it.id == telefone.id } as Telefone
            if(fone){
                fone.ddd = telefone.ddd
                fone.numero = telefone.numero
                fone.tipo = telefone.tipo
            }else{
                usuario.addTelefone telefone
            }
        }
        def telefonesARemover = new ArrayList()
        usuario.telefones.each {Telefone telefone ->
            TelefoneDTO fone = usuarioDTO.telefones.find { it.id == telefone.id } as TelefoneDTO
            if(!fone)
                telefonesARemover << telefone
        }
        telefonesARemover.each {Telefone it ->
            usuario.removeFromTelefones it
        }
        return usuario
    }

    static crieUsuarioDTOs(List<Usuario> usuarios){
        return usuarios.collect {crieUsuarioDTO(it)}
    }
}