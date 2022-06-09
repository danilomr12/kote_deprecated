package br.com.cotecom.services.remote

import br.com.cotecom.domain.error.ErrorHandler
import br.com.cotecom.domain.error.ErrorHandlerFactory
import br.com.cotecom.domain.error.ServerError
import br.com.cotecom.domain.usuarios.Comprador
import br.com.cotecom.dtos.assembler.UsuarioAssembler
import org.apache.log4j.Logger
import br.com.cotecom.domain.usuarios.Representante
import br.com.cotecom.domain.usuarios.Estoquista
import br.com.cotecom.domain.usuarios.Usuario

class RemoteAuthService {

    private static final log = Logger.getLogger(RemoteAuthService.class)
    private ErrorHandler errorHandler = ErrorHandlerFactory.getErrorHandler(ErrorHandlerFactory.DEFAULT_ERROR_HANDLER)

    boolean transactional = true

    static expose = ["flex-remoting"]

    def usuarioService

    def doLogin(String username, String password) {
        def out = usuarioService.doLogin(username, password, false)

        if (out instanceof Usuario)
            return UsuarioAssembler.crieUsuarioDTO(out)
        else
            return errorHandler?.pushAndDispatchError(
                    new ServerError(id: ServerError.UNSUPORTED_OPERATION_ID,
                            acao: "Realizar Login",
                            mensagem: "Não foi possível realizar o login.",
                            causa: "Usuario ou Senha Inválidos."))

    }


}
