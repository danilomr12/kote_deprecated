package br.com.cotecom.services.remote

import br.com.cotecom.domain.dto.usuario.AtendimentoDTO
import br.com.cotecom.domain.dto.usuario.EmpresaDTO
import br.com.cotecom.domain.dto.usuario.UsuarioDTO
import br.com.cotecom.domain.usuarios.Comprador
import br.com.cotecom.domain.usuarios.Representante
import br.com.cotecom.domain.usuarios.Usuario
import br.com.cotecom.domain.usuarios.UsuarioSupervisor
import br.com.cotecom.domain.usuarios.empresa.Atendimento
import br.com.cotecom.dtos.assembler.EmpresaAssembler
import br.com.cotecom.dtos.assembler.UsuarioAssembler
import br.com.cotecom.services.UsuarioService
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

class RemoteUsuarioService {

    static expose = ["flex-remoting"]
    UsuarioService usuarioService
    def springSecurityService
    def notifierService

    public UsuarioDTO getUsuarioLogado(){
        Usuario usuario = usuarioService.getSessionUser() as Usuario;
        if(!usuario)
            return null
        UsuarioDTO usuarioDTO = UsuarioAssembler.crieUsuarioDTO(usuario)
        usuarioDTO.isSwitched = SpringSecurityUtils.isSwitched()
        return usuarioDTO
    }

    public List<UsuarioDTO> getRepresentantesByComprador(){
        def usuarioLogado = usuarioService.getSessionUser()
        List<UsuarioDTO> representanteDTOs = usuarioService.getRepresentantesByComprador(usuarioLogado).collect { Representante representante ->
            UsuarioAssembler.crieUsuarioDTO(representante)
        }
        return representanteDTOs
    }

    public List<AtendimentoDTO> getAtendimentosByComprador(){
        Comprador comprador = Comprador.get(usuarioLogado.id)
        return UsuarioAssembler.crieAtendimentosDTO(usuarioService.getAtendimentosByComprador(comprador))
    }

    public List<UsuarioDTO> getSupervisores(){
        UsuarioAssembler.crieUsuarioDTOs(usuarioService.getSupervisores())
    }

    public List<EmpresaDTO> getFornecedores(){
        return EmpresaAssembler.crieEmpresaDTOs(usuarioService.getFornecedores())
    }

    public List<UsuarioDTO> busqueNovosRepresentantes(String query){
        return usuarioService.busqueNovosRepresentantes(Comprador.get(usuarioLogado.id), query).collect {
            Representante representante -> UsuarioAssembler.crieUsuarioDTO(representante)
        }
    }

    public UsuarioDTO saveRepresentante(UsuarioDTO representanteDTO){
        def repSalvo = usuarioService.saveUsuario(UsuarioAssembler.crieUsuario(representanteDTO))
        if(representanteDTO.supervisor != null){
            def supervisor = usuarioService.saveUsuario(
                    UsuarioAssembler.crieUsuario(
                            representanteDTO.supervisor
                    )
            )
            if(representanteDTO?.supervisor?.id != repSalvo?.supervisor?.id){
                UsuarioSupervisor.findByUsuario(repSalvo)?.delete(flush: true)
                supervisor.addRepresentante(repSalvo)
            }
        }
        if(representanteDTO?.empresa?.id != repSalvo?.empresa?.id){
            usuarioService.atualizeFornecedorDeAtendimentos(repSalvo)
        }
        UsuarioAssembler.crieUsuarioDTO(repSalvo)
    }

    public def crieAtendimento(UsuarioDTO representanteDTO){
        if(representanteDTO != null){
            def atendimento = usuarioService.crieAtendimento(
                    UsuarioAssembler.crieUsuario(representanteDTO),
                    usuarioService.getSessionUser()
            )
            if(atendimento instanceof Atendimento){
                return UsuarioAssembler.crieUsuarioDTO(
                        atendimento.representante
                )
            }else{
                return atendimento
            }
        }
        return null
    }

    public EmpresaDTO saveEmpresa(EmpresaDTO empresaDTO){
        return EmpresaAssembler.crieEmpresaDTO(
                usuarioService.salveEmpresa(
                        EmpresaAssembler.crieEmpresa(
                                (EmpresaDTO)empresaDTO
                        )
                )
        );
    }

    public UsuarioDTO saveSupervisor(UsuarioDTO supervisorDTO){
        return UsuarioAssembler.crieUsuarioDTO(
                usuarioService.saveUsuario(
                        UsuarioAssembler.crieUsuario(supervisorDTO)
                )
        )
    }

    public void removeRepresentante(UsuarioDTO representanteDTO){
        usuarioService.deleteRepresentante(representanteDTO.id)
    }

    public boolean saveComprador(UsuarioDTO usuarioDTO) {
        if(usuarioService.saveUsuario(UsuarioAssembler.crieUsuario(usuarioDTO)))
            return true
        return false
    }

    public Boolean removeAtendimento(UsuarioDTO representanteDTO){
        def comprador = usuarioService.sessionUser as Comprador
        def representante = UsuarioAssembler.crieUsuario(representanteDTO) as Representante
        usuarioService.removaAtendimento(representante,comprador)
        return true
    }
}
