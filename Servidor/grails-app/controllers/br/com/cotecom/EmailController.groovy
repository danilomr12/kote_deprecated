package br.com.cotecom

import br.com.cotecom.domain.resposta.Resposta
import br.com.cotecom.domain.usuarios.Comprador
import br.com.cotecom.domain.usuarios.Representante
import br.com.cotecom.domain.pedido.Pedido

class EmailController {

    def notificacaoNovaCotacao ={
        Resposta resposta = Resposta.get(params?.id)
        def urlResposta = getServerRootUrl()
        render (view: "/mail/resposta", model: [emailComprador: resposta.cotacao.empresa.comprador.email,
                usuario: resposta.representante.nome, mensagem: resposta.cotacao.mensagem,
                mailTemplate: false, nomeFantasiaCliente: resposta.cotacao.empresa.nomeFantasia])
    }

    def notificacaoNovoPedido ={
        Comprador comprador = Comprador.read(params?.compradorId)
        Representante representante = Representante.read(params?.representanteId)
        String urlPedido = getServerRootUrl() + "/pedido/exportePedidoExcel?digest=${params?.digest}"

        render (view: "/mail/novoPedido", model:[ nomeRepresentante: representante?.nome,
                nomeFantasiaCliente: comprador?.empresa?.nomeFantasia, urlPedido: urlPedido,
                                    comprador: comprador, mailTemplate: false])
    }

    def notificacaoCadastro ={
        Comprador comprador = Comprador.read(params?.compradorId)
        Representante representante = Representante.read(params?.representanteId)
        render (view: "/mail/infoCadastroPorComprador", model:[ usuario: representante,
                comprador: comprador, mailTemplate: false, emailComprador: comprador.email])
    }

    def notificacaoRecuperaSenha = {
        String urlNovaSenha = getServerRootUrl() + "/login/createNewPassword?token=${params?.token}"
        render (view: "/mail/recuperaSenha", model: [nomeUsuario: params?.nomeUsuario, urlNovaSenha: urlNovaSenha, mailTemplate: false])
    }

    private String getServerRootUrl() {
        String serverURL = grailsApplication.config.grails.serverURL
        String root = ""
        int barrasCout = 0
        serverURL.each {
            if(it=="/")
                barrasCout ++
            if(barrasCout<3)
                root += it
        }
        return root
    }
}
