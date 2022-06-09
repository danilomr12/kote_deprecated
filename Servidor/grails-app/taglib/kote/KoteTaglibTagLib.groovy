package kote

class KoteTaglibTagLib {

    def usuarioService

    def sayHelloToUser = { attribs ->
        def nomeDoUsuario = usuarioService.getSessionUser().nome
        out << "Olá ${nomeDoUsuario}"
    }

}
