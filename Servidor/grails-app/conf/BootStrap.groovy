import br.com.cotecom.domain.usuarios.Administrador
import br.com.cotecom.domain.usuarios.Comprador
import br.com.cotecom.domain.usuarios.Requestmap
import br.com.cotecom.domain.usuarios.Responsabilidade
import br.com.cotecom.domain.usuarios.empresa.Cliente
import br.com.cotecom.domain.usuarios.empresa.CoteCom
import br.com.cotecom.domain.usuarios.empresa.Endereco
import grails.util.GrailsUtil
import org.apache.log4j.Logger
import org.codehaus.groovy.grails.commons.GrailsApplication

class BootStrap {

    private static final log = Logger.getLogger(BootStrap.class)

    def springSecurityService
    def emailConfirmationService
    def usuarioService
    def fileService

    def init = { servletContext ->
        emailConfirmationService.onConfirmation = { email, uid ->
            log.info("User with id $uid has confirmed their email address $email")
            def comprador = Comprador.findByEmail(email)
            if(comprador){
                comprador.habilitado = true
                comprador.save(flush:true)
                usuarioService.doLogin(comprador.email, comprador.password, false)
            }
            def params = [email:email, password:comprador.password]
            return [controller:'register', action:'confirmed', params:params]
        }
        emailConfirmationService.onInvalid = { uid ->
            log.warn("User with id $uid failed to confirm email address after 30 days")
        }
        emailConfirmationService.onTimeout = { email, uid ->
            log.warn("User with id $uid failed to confirm email address after 30 days")
        }

        fileService.startOpenOfficeProcess()
        switch (GrailsUtil.environment) {
            case "development":
                setRoles()
                setRequestMap()
                createDefaultEmpresas()
                createDefaultUsers()
                inicializeClassesDeDominio(servletContext)
                break
            case "test":
                break
            case "production":
                setRoles()
                setRequestMap()
                createDefaultEmpresas()
                createDefaultUsers()
                inicializeClassesDeDominio(servletContext)
                break
        }
        log.info("Bootstrap carregado com exito.")
    }

    private def inicializeClassesDeDominio(servletContext) {
        def application = servletContext.getAttribute(GrailsApplication.APPLICATION_ID)

        application.domainClasses.each {dc ->
            dc.metaClass.saveNow << {->
                def retorno = delegate.save(flush:true)
                Logger.getLogger(delegate.getClass()).debug "Tentando salvar objeto ${delegate.toString().replace('\n', '')}"
                if (!retorno)
                    Logger.getLogger(delegate.getClass()).error "O objeto ${delegate.getClass().name} não pode ser salvo. Erros: ${delegate.errors.allErrors.join("||")}"
                return retorno
            }
        }
    }

    private def createDefaultEmpresas() {
        def kote = CoteCom.findByEmail("contato@idra.com.br")
        if(!kote){
            kote = new CoteCom(nomeFantasia:'IDRA Consultoria', razaoSocial:'IDRA LTDA', email:"contato@idra.com.br")
            kote.save(flush:true)
        }
        def demo = Cliente.findByEmail("empresademo@kote.com.br")
        if(!demo){
            def enderecos = new HashSet()
            demo = new Cliente(nomeFantasia:'Kote', razaoSocial:'KoteLTDA', email:"empresademo@kote.com.br")
            demo.endereco = new Endereco(logradouro: "Rua demonstração", numero: "341", bairro: "Setor demo", estado: "GO",
                    cidade: "Cidade demonstração").save(flush:true)
            demo.save(flush:true)
        }
    }

    private def createDefaultUsers() {
        if(!Administrador.findByEmail("admin@kote.com.br")){
            Administrador admin = new Administrador(nome: "admin", password: springSecurityService.encodePassword("akdomtien"),
                    email: "admin@kote.com.br", habilitado: true)
            admin.save(flush:true)

            admin.empresa = CoteCom.findByEmail("contato@kote.com.br")
            if(admin.save(flush:true)){
                admin.addResponsabilidade(Responsabilidade.findByResponsabilidade("ROLE_ADMIN"))
                log.info("Usuario administrador criado com sucesso. User: admin Email: admin (at) kote.com.br")
            }
            else{
                log.error(admin.errors.each { it.toString() })
            }

        }
        if(!Comprador.findByEmail("demo@kote.com.br")){
            Comprador comprador = new Comprador(nome: "demo", password: springSecurityService.encodePassword("demo"),
                    email: "demo@kote.com.br", habilitado: true)
            comprador.save(flush:true)

            def empDemo = Cliente.findByEmail("empresademo@kote.com.br")
            if(!empDemo)
                empDemo = new Cliente(nomeFantasia: "empresa demo", email: "empresademo@kote.com.br")
            comprador.empresa = empDemo
            comprador.clientes = []
            comprador.addToClientes(empDemo)
            if (comprador.save(flush:true)) {
                comprador.addResponsabilidade(Responsabilidade.findByResponsabilidade("ROLE_COMPRADOR"))
                log.info("Usuario de demonstracao criado com sucesso. User: admin Email: demo (at) kote.com.br")
            }
            else{
                log.error(comprador.errors.each { it.toString() })
            }
        }
    }

    private def setRoles() {
        if(!Responsabilidade.findByResponsabilidade("ROLE_ADMIN")){
            new Responsabilidade(responsabilidade: "ROLE_ADMIN", descricao: "Papel administradores").save(flush: true)
            log.info "Responsabilidade ADMIN carregada com sucesso."
        }
        if(!Responsabilidade.findByResponsabilidade("ROLE_COMPRADOR")){
            new Responsabilidade(responsabilidade: "ROLE_COMPRADOR", descricao: """Papel cliente. Na maioria dos casos usuários
            supermercado""").save(flush: true)
            log.info "Responsabilidade COMPRADOR carregada com sucesso."
        }
        if(!Responsabilidade.findByResponsabilidade("ROLE_REPRESENTANTE")){
            new Responsabilidade(responsabilidade: "ROLE_REPRESENTANTE", descricao: "Representante de vendas. Responde cotação").save(flush: true)
            log.info "Responsabilidade REPRESENTANTE carregada com sucesso."
        }
        if(!Responsabilidade.findByResponsabilidade("ROLE_ESTOQUISTA")){
            new Responsabilidade(responsabilidade: "ROLE_ESTOQUISTA", descricao: "Controlador de estoque do cliente. cria lista de compras para cotação").save(flush: true)
            log.info "Responsabilidade ESTOQUISTA carregada com sucesso."
        }
    }

    private def setRequestMap() {
        if(!Requestmap.findByUrl("/contact/**"))
            new Requestmap(url: "/contact/**", configAttribute: "ROLE_ADMIN").save(flush: true)
        if(!Requestmap.findByUrl("/register/**"))
            new Requestmap(url: "/register/**", configAttribute: "ROLE_ADMIN").save(flush: true)
        if(!Requestmap.findByUrl("/app/representante"))
            new Requestmap(url: "/app/representante", configAttribute: "ROLE_ADMIN, ROLE_REPRESENTANTE").save(flush: true)
        if(!Requestmap.findByUrl("/app/comprador"))
            new Requestmap(url: "/app/comprador", configAttribute: "ROLE_ADMIN, ROLE_COMPRADOR").save(flush: true)
        if(!Requestmap.findByUrl("/app/estoquista"))
            new Requestmap(url: "/app/estoquista", configAttribute: "ROLE_ADMIN, ROLE_ESTOQUISTA").save(flush: true)
        if(!Requestmap.findByUrl("/flex/**"))
            new Requestmap(url: "/flex/**", configAttribute: "IS_AUTHENTICATED_ANONYMOUSLY").save(flush: true)
        if(!Requestmap.findByUrl("/assets/**"))
            new Requestmap(url: "/assets/**", configAttribute: "IS_AUTHENTICATED_ANONYMOUSLY").save(flush: true)
        if(!Requestmap.findByUrl("/css/**"))
            new Requestmap(url: "/css/**", configAttribute: "IS_AUTHENTICATED_ANONYMOUSLY").save(flush: true)
        if(!Requestmap.findByUrl("/br/**"))
            new Requestmap(url: "/br/**", configAttribute: "IS_AUTHENTICATED_ANONYMOUSLY").save(flush: true)
        if(!Requestmap.findByUrl("/skins/**"))
            new Requestmap(url: "/skins/**", configAttribute: "IS_AUTHENTICATED_ANONYMOUSLY").save(flush: true)
        if(!Requestmap.findByUrl("/history/**"))
            new Requestmap(url: "/history/**", configAttribute: "IS_AUTHENTICATED_ANONYMOUSLY").save(flush: true)
        if(!Requestmap.findByUrl("/images/**"))
            new Requestmap(url: "/images/**", configAttribute: "IS_AUTHENTICATED_ANONYMOUSLY").save(flush: true)
        if(!Requestmap.findByUrl("/js/**"))
            new Requestmap(url: "/js/**", configAttribute: "IS_AUTHENTICATED_ANONYMOUSLY").save(flush: true)
        if(!Requestmap.findByUrl("/login/**"))
            new Requestmap(url: "/login/**", configAttribute: "IS_AUTHENTICATED_ANONYMOUSLY").save(flush: true)
        if(!Requestmap.findByUrl("/logout/**"))
            new Requestmap(url: "/logout/**", configAttribute: "IS_AUTHENTICATED_REMEMBERED").save(flush: true)
        if(!Requestmap.findByUrl("/test/**"))
            new Requestmap(url: "/test/**", configAttribute: "ROLE_ADMIN").save(flush: true)
        if(!Requestmap.findByUrl("/comprador/**"))
            new Requestmap(url: "/comprador/**", configAttribute: "ROLE_ADMIN").save(flush: true)
        if(!Requestmap.findByUrl("/cliente/**"))
            new Requestmap(url: "/cliente/**", configAttribute: "ROLE_ADMIN").save(flush: true)
        if(!Requestmap.findByUrl("/monitoring/**"))
            new Requestmap(url: "/monitoring/**", configAttribute: "ROLE_ADMIN").save(flush: true)
        if(!Requestmap.findByUrl("/import/**"))
            new Requestmap(url: "/import/**", configAttribute: "ROLE_ADMIN").save(flush: true)
        if(!Requestmap.findByUrl("/monitoring/**"))
            new Requestmap(url: "/monitoring/**", configAttribute: "ROLE_ADMIN").save(flush: true)
        if(!Requestmap.findByUrl("/**Admin/**"))
            new Requestmap(url: "/**Admin/**", configAttribute: "ROLE_ADMIN").save(flush: true)

        log.info("Request Map persistido com exito. Regras para acesso de usuarios criadas.")
    }

    def destroy = {
        fileService.stopOpenOfficeProcess()
    }

}
