package br.com.cotecom.aplicacao

import br.com.cotecom.domain.usuarios.Usuario

class AppController {

    def usuarioService
    def index = { }

    def comprador = {
        render view: "comprador"
    }

    def representante = {
        render(view: "representante")
    }

    def estoquista = {
        render view: "estoquista"
    }
}
