package br.com.cotecom.services

import org.springframework.security.core.context.SecurityContextHolder as SCH
import org.springframework.web.context.request.RequestContextHolder as RCH

import br.com.cotecom.domain.usuarios.Comprador
import br.com.cotecom.domain.usuarios.Representante
import br.com.cotecom.domain.usuarios.Supervisor
import br.com.cotecom.domain.usuarios.Usuario
import br.com.cotecom.domain.usuarios.empresa.Atendimento
import br.com.cotecom.domain.usuarios.empresa.Cliente
import br.com.cotecom.domain.usuarios.empresa.Empresa
import br.com.cotecom.domain.usuarios.empresa.Fornecedor
import org.apache.log4j.Logger
import org.codehaus.groovy.grails.plugins.springsecurity.GrailsUser
import org.codehaus.groovy.grails.plugins.springsecurity.GrailsUserDetailsService
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.GrantedAuthorityImpl
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.AuthenticationProcessingFilter
import org.springframework.security.web.authentication.WebAuthenticationDetails
import br.com.cotecom.domain.error.ServerError
import br.com.cotecom.domain.error.ErrorHandler

import br.com.cotecom.domain.usuarios.Responsabilidade
import org.springframework.security.core.context.SecurityContextHolder
import org.codehaus.groovy.grails.commons.ApplicationHolder
import br.com.analise.service.ICompraService

public class UsuarioService implements GrailsUserDetailsService {

    private static final logger = Logger.getLogger(UsuarioService.class)

    def compraService
    def springSecurityService
    def rememberMeServices
    def authenticationManager
    def notifierService
    ErrorHandler errorHandler = ErrorHandler.getInstance()

    static final List NO_ROLES = [new GrantedAuthorityImpl(SpringSecurityUtils.NO_ROLE)]

    UserDetails loadUserByUsername(String email, boolean loadRoles) throws UsernameNotFoundException {
        return loadUserByUsername(email)
    }

    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Usuario.withTransaction { status ->
            Usuario user = Usuario.findByEmail(email)
            if (!user)
                throw new UsernameNotFoundException('User not found', email)

            def authorities = user.responsabilidades.collect {new GrantedAuthorityImpl(it.responsabilidade)}

            return new GrailsUser(user.email, user.password, user.habilitado, !user.accountExpired,
                    !user.passwordExpired, !user.accountLocked,
                    authorities ?: NO_ROLES, user.id)
        }
    }

    def doLogin(email, password, rememberMe) {
        if (!email)
            return "Email n&atilde;o pode ser vazio."

        if (!password)
            return "A senha n&atilde;o pode ser vazia."

        def user = Usuario.findByEmail(email)
        if (!user)
            return "Usu&aacute;rio/Senha inv&aacute;lido(s)"

        def validate = validateUser(user)
        if(validate instanceof String)
            return validate

        if (user.password == springSecurityService.encodePassword(password)) {
            def request = RCH.currentRequestAttributes().currentRequest
            def response = RCH.currentRequestAttributes().currentResponse
            def responsabilidades = []

            user.responsabilidades.each { role -> responsabilidades << new GrantedAuthorityImpl(role.responsabilidade)}

            def userDetails = loadUserByUsername(user.email)

            def authentication = new UsernamePasswordAuthenticationToken(userDetails, password, responsabilidades as GrantedAuthority[])

            authentication.details = new WebAuthenticationDetails(request)
            SCH.context.authentication = authentication
            request.session.setAttribute(AuthenticationProcessingFilter.SPRING_SECURITY_LAST_USERNAME_KEY, user.email)

            authenticationManager.authenticate(authentication)

            if (rememberMe)
                rememberMeServices.loginSuccess request, response, authentication

            return user
        }
        return "Usu&aacute;rio/Senha inv&aacute;lido(s)"
    }

    // TODO Verificar compatibilidade com flex

    def Usuario getSessionUser() {
        def userDetails = springSecurityService.principal
        def usuario
        if(userDetails != "anonymousUser")
            usuario = Usuario.get(userDetails.id)
        if (!usuario)
            return null
        if (!usuario.id)
            return null
        return usuario
    }

    public List<Atendimento> getAtendimentosByComprador(Comprador comprador) {
        if (!comprador)
            return null
        List<Atendimento> atendimentos = new ArrayList()
        if (comprador.empresa)
            atendimentos = Atendimento.findAllByCliente(comprador.empresa as Cliente)
        return atendimentos
    }

    public List<Usuario> getSupervisores() {
        return Supervisor.list();
    }

    public List<Fornecedor> getFornecedores() {
        return Fornecedor.list();
    }

    public List<Representante> getRepresentantesByComprador(Comprador comprador) {
        if (!comprador)
            return null

        List<Atendimento> atendimentos = Atendimento.findAllByCliente(comprador.empresa as Cliente)

        List<Representante> representantes = new ArrayList<Representante>()
        atendimentos?.each { Atendimento atendimento ->
            representantes.push(atendimento.representante)
        }
        return representantes
    }

    public List<Representante> busqueNovosRepresentantes(Comprador comprador, String query) {
        def results
        def criteria = Representante.createCriteria()
        if (query == null)
            return null
        if (query.length() == 0) {
            results = results = criteria.list {
                eq("isDeletado", false)
                maxResults(30)
                order("nome", "asc")
            }
            return retorneRepresentantesSemAtendimento(results, comprador)
        }
        def newQuery = "%${query}%"
        results = criteria.list {
            eq("isDeletado", false)
            or {
                ilike("nome", newQuery)
                ilike("email", newQuery)
                empresa {
                    or {
                        ilike("email", newQuery)
                        ilike("nomeFantasia", newQuery)
                        ilike("razaoSocial", newQuery)
                    }
                }
            }
            maxResults(30)
            order("nome", "asc")
        }
        if (results.isEmpty())
            return null
        return retorneRepresentantesSemAtendimento(results, comprador)
    }

    private List<Representante> retorneRepresentantesSemAtendimento(representantes, Comprador comprador) {
        List<Representante> representantesSemAtendimento = new ArrayList<Representante>()

        representantes.each {Representante representante ->
            if (!Atendimento.findByRepresentanteAndCliente(representante, comprador.empresa as Cliente))
                representantesSemAtendimento.add(representante)
        }
        return representantesSemAtendimento
    }

    public Empresa salveEmpresa(Empresa empresa) {
        if (!empresa)
            return null
        return empresa.save(flush: true)
    }

    public void deleteRepresentante(Long id) {
        if (id != null)
            Representante.get(id).delete()
    }

    public Atendimento salveAtendimento(Atendimento atendimento) {
        if (!atendimento)
            return null
        return atendimento.save(flush: true)
    }

    public def saveUsuario(Usuario usuario) {
        if (!usuario)
            return null
        if (usuario.save()) {
            return usuario
        }
        logger.error(usuario.errors.each { it.toString() })
        return errorHandler?.pushAndDispatchError(new ServerError(id: ServerError.UNSUPORTED_OPERATION_ID,
                acao: "Adicionar representante",
                mensagem: "O e-mail informado já está cadastrado no sistema, experimente a busca de fornecedores usando o e-mail informado",
                causa: "O representante já possui cadastro no sistema."))
    }

    void atualizeFornecedorDeAtendimentos(Usuario representante) {
        List<Atendimento> atendimentos = Atendimento.findAllByRepresentante(representante)
        atendimentos.each {Atendimento atendimento ->
            if (atendimento?.fornecedor?.id != representante?.empresa?.id) {
                atendimento.fornecedor = representante.empresa
                salveAtendimento(atendimento)
            }
        }
    }

    private def crieNovoRepresentante(Usuario representante) {
        def password = new Date().time.toString().encodeAsUrlDigest().substring(0,7)
        representante.password = springSecurityService.encodePassword(password)
        representante.setHabilitado(true)
        def usuario = saveUsuario(representante)
        if(usuario instanceof Usuario){
            representante.addResponsabilidade(Responsabilidade.findByResponsabilidade("ROLE_REPRESENTANTE"))
            return password
        }else{
            return usuario
        }

    }

    def crieAtendimento(Usuario representante, Usuario comprador) {
        def tempPassword = ""
        if (!representante.id){
            tempPassword = crieNovoRepresentante(representante)
            if (!tempPassword || tempPassword instanceof ArrayList)
                return tempPassword
        }
        Atendimento atendimento = Atendimento.findByClienteAndRepresentante(comprador.empresa as Cliente, representante as Representante)
        if (!atendimento) {
            atendimento = new Atendimento(representante: representante as Representante,
                    cliente: comprador.empresa as Cliente,
                    fornecedor: representante.empresa as Fornecedor)
        }
        atendimento = salveAtendimento(atendimento)
        if (atendimento.id) {
            notifierService.notifiqueCadastroRepresentantePeloComprador(comprador, representante, tempPassword)
            return atendimento
        }
        return null
    }

    def removaAtendimento(Representante representante, Comprador comprador){
        Atendimento atendimento = Atendimento.findByClienteAndRepresentante(comprador.empresa as Cliente, representante)
        if(!atendimento){
             return errorHandler?.pushAndDispatchError(new ServerError(id: ServerError.UNSUPORTED_OPERATION_ID,
                        acao: "Remover atendimento",
                        mensagem: "Este representante não te atende mais",
                        causa: "Provavelmente já foi removido e sua tela não está atualizada"))
        }
        atendimento.delete()
    }

    def mudeSenha(String emailUsuario, String senhaAtual, String novaSenha){
        if (!emailUsuario)
            return "Email n&atilde;o pode ser vazio."
        if (!novaSenha)
            return "A nova senha n&atilde;o pode ser vazia."
        Usuario usuario = Usuario.findByEmail(emailUsuario)
        if (!usuario)
            return "Usu&aacute;rio/Senha inv&aacute;lido(s)"
        if(usuario.password == springSecurityService.encodePassword(senhaAtual)){
            usuario.password = springSecurityService.encodePassword(novaSenha)
            usuario.newUser = false
            return usuario.save(flush:true)
        }else{
            return "Usu&aacute;rio/Senha inv&aacute;lido(s)"
        }
    }

    public Double calculeComprasMesAtual(long empresaId) {
        def hoje = new Date()
        hoje.month
        Calendar primeiroDiaMesAtual = GregorianCalendar.getInstance()
        primeiroDiaMesAtual.set(hoje.year, hoje.month, 1)
        def ultimoDiaDoMesAtual = primeiroDiaMesAtual.getActualMaximum(Calendar.DAY_OF_MONTH)
        Calendar ultimoMesAtual = GregorianCalendar.instance
        ultimoMesAtual.set(hoje.year, hoje.month, ultimoDiaDoMesAtual)
        compraService.getTotalComprasDoPeriodo(primeiroDiaMesAtual.time, ultimoMesAtual.time, empresaId)
    }

    public Double calculeComprasMesAnterior(long empresaId) {
        def dateHoje = new Date()
        def dateMesPassado = dateHoje
        Calendar primeiroDiaMesAnterior = GregorianCalendar.getInstance()
        if(dateHoje.month == Calendar.JANUARY){
            dateMesPassado.month = Calendar.DECEMBER
            dateMesPassado.year = dateHoje.year-1
        }else{
            dateMesPassado.month = dateHoje.month - 1
        }
        primeiroDiaMesAnterior.set(dateMesPassado.year, dateMesPassado.month, 1)
        def ultimoDiaDoMesAtual = primeiroDiaMesAnterior.getActualMaximum(Calendar.DAY_OF_MONTH)
        Calendar ultimoMesAnterior = GregorianCalendar.instance
        ultimoMesAnterior.set(dateMesPassado.year, dateMesPassado.month, ultimoDiaDoMesAtual)
        compraService.getTotalComprasDoPeriodo(primeiroDiaMesAnterior.time, ultimoMesAnterior.time, empresaId)
    }

    def validateUser(Usuario usuario) {
        def result = true
        if(usuario.accountExpired)
            result = "Conta expirada"
        if(usuario.accountLocked)
            result = "Conta bloqueada"
        if(!usuario.habilitado)
            result = "Usuário bloqueado"
        if(usuario.passwordExpired)
            result = "Senha expirada"
        if(usuario.isDeletado)
            result = "Usuário deletado"
        return result
    }

}