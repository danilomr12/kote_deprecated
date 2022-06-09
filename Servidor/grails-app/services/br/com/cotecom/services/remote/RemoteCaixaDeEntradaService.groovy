package br.com.cotecom.services.remote

import br.com.cotecom.domain.dto.tela.TelaCaixaDeEntradaDTO

import br.com.cotecom.dtos.assembler.UsuarioAssembler
import br.com.cotecom.services.CaixaDeEntradaService
import org.apache.log4j.Logger
import br.com.cotecom.domain.dto.usuario.UsuarioDTO

class RemoteCaixaDeEntradaService {

    private static final log = Logger.getLogger(RemoteCaixaDeEntradaService.class)

    boolean transactional = true
    static expose = ["flex-remoting"]

    CaixaDeEntradaService caixaDeEntradaService

    def loadCaixaDeEntrada(UsuarioDTO usuario){
        return new TelaCaixaDeEntradaDTO(
                itensCaixaDeEntrada: caixaDeEntradaService.loadCaixaDeEntrada(UsuarioAssembler.crieUsuario(usuario)))
    }

}
