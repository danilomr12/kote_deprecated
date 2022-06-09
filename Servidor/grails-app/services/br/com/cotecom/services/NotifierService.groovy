package br.com.cotecom.services

import br.com.cotecom.domain.serviceinterfaces.INotifierService

import org.apache.log4j.Logger
import org.hibernate.Hibernate
import org.springframework.mail.MailException
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import br.com.cotecom.domain.pedido.Pedido

import br.com.cotecom.domain.usuarios.Usuario

import br.com.cotecom.domain.usuarios.security.RegistrationCode

import br.com.analise.domain.RespostaCompra

public class NotifierService implements INotifierService{

    private static final log = Logger.getLogger(NotifierService.class)

    static transactional = false

    def grailsTemplateEngineService
    def config = ConfigurationHolder.config

    void notifiqueNovaCotacao(String emailComprador, String nomeRepresentante, String emailRepresentante, Long respostaId,
                              String mensagem, String nomeFantasiaCliente) {
        Thread.start {
            try{
                String url = getServerRootUrl()
                String urlApp = url
                url += "/email/notificacaoNovaCotacao?id=${respostaId.encodeAsURL()}"
                String template = grailsTemplateEngineService.renderView("/mail/resposta", [emailComprador: emailComprador,
                        usuario:nomeRepresentante, url: url, mensagem: mensagem, urlApp: urlApp, nomeFantasiaCliente: nomeFantasiaCliente,
                        mailTemplate: true])
                envieSesEmail(emailRepresentante, "Nova Cotacao - Aguardando Resposta", template)
            }catch(MailException m){
                log.debug(m.getStackTrace())
            }
        }
    }

    def notifiqueCriacaoUsuario(String email, String nome){
        Thread.start {
            try{
                String template = grailsTemplateEngineService.renderView("/mail/contactMe", [url:getServerRootUrl(),
                        usuario:nome])
                envieSesEmail(email, "Confirmar cadastro - Kote", template)
            }
            catch(MailException m){
                log.debug(m.getStackTrace())
            }
        }
    }

    def notifiqueCadastroRepresentantePeloComprador(Usuario comprador, Usuario representante, def password){
        String urlApp = getServerRootUrl()
        String url = urlApp + "/email/notificacaoCadastro?compradorId=${comprador.id}&"+
                "representanteId=${representante.id}"

        Hibernate.initialize comprador
        Hibernate.initialize representante
        String email = representante.email
        Thread.start {
            try{
                String template = grailsTemplateEngineService.renderView("/mail/infoCadastroPorComprador",
                        [url: url, usuario: representante, comprador: comprador, mailTemplate: true, newUser:
                                representante.newUser, password: password, urlApp: urlApp, emailComprador: comprador.email])
                envieSesEmail(email,"Cadastro - Kote", template)
            }
            catch(MailException m){
                log.debug m.getStackTrace()
            }
        }
    }

    boolean notifiquePedidoParaRepresentante(Pedido pedido, RespostaCompra respostaCompra, String nomeComprador, String emailComprador, Long idComprador, String nomeFantasiaCliente){
        String nomeRepresentante = respostaCompra?.nomeRepresentante
        boolean result = true
        String url = getServerRootUrl()
        String urlPedido = url + "/pedido/exportePedidoExcel?digest=${pedido.pedidoUrlDigest.encodeAsURL()}"
        url += "/email/notificacaoNovoPedido?representanteId=${respostaCompra.idRepresentante}&compradorId=${idComprador}&digest=${pedido.pedidoUrlDigest}"

        try{
            String template = grailsTemplateEngineService.renderView("/mail/novoPedido",
                    [url: url, nomeRepresentante: nomeRepresentante, nomeFantasiaCliente: nomeFantasiaCliente,
                            nomeComprador: nomeComprador, mailTemplate: true, urlPedido: urlPedido, emailComprador: emailComprador])
            if(envieSesEmail(respostaCompra.emailRepresentante,"Novo Pedido - Kote" + " - " + nomeFantasiaCliente, template))
                result = true
        }
        catch(MailException m){
            log.debug(m.getStackTrace())
            result = false
            return result
        }
        finally {
            return result
        }
    }

    def notifiqueRecuperacaoDeSenha(RegistrationCode registrationCode, Usuario usuario) {
        def result = true
        String urlApp = getServerRootUrl()
        String urlNovaSenha = urlApp + "/login/createNewPassword?token=${registrationCode?.token}"
        String url = urlApp + "/email/notificacaoRecuperaSenha?token=${registrationCode?.token}&nomeUsuario=${usuario?.nome}"
        try{
            String template = grailsTemplateEngineService.renderView("/mail/recuperaSenha",
                    [url: url, urlNovaSenha: urlNovaSenha, nomeUsuario: usuario.nome, mailTemplate: true])
            if(envieSesEmail(usuario.email, "Você solicitou recuperação de senha do Kote", template))
                result = true
        }catch(MailException m){
            log.debug(m.getStackTrace())
            result = false
            return result
        }finally{
            return result
        }
    }

    private String getServerRootUrl() {
        String serverURL = config.grails.serverURL
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

    def envieSesEmail(String para, String assunto, String htmlTemplate){
        return sesMail {
            to para
            subject assunto
            html htmlTemplate
        }
    }

}