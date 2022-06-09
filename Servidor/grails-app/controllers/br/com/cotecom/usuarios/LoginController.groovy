package br.com.cotecom.usuarios

import org.springframework.security.core.context.SecurityContextHolder as SCH

import br.com.cotecom.domain.usuarios.Administrador
import br.com.cotecom.domain.usuarios.Comprador
import br.com.cotecom.domain.usuarios.Usuario
import grails.converters.JSON
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.springframework.security.authentication.AccountExpiredException
import org.springframework.security.authentication.CredentialsExpiredException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import br.com.cotecom.domain.usuarios.Estoquista
import br.com.cotecom.domain.usuarios.Representante
import br.com.cotecom.domain.usuarios.security.RegistrationCode

class LoginController {

    /**
     * Dependency injection for the authenticationTrustResolver.
     */
    def authenticationTrustResolver

    /**
     * Dependency injection for the springSecurityService.
     */
    def springSecurityService

    def usuarioService
    def notifierService

    /**
     * Default action; redirects to 'defaultTargetUrl' if logged in, /login/auth otherwise.
     */
    def index = {
        if (springSecurityService.isLoggedIn()) {
            def sessionUser = usuarioService.sessionUser
            if (sessionUser.isNewUser()){
                redirect(controller: 'login', action: 'changePass')
            }else if(sessionUser instanceof Representante){
                redirect(controller: 'app', action: 'representante')
            }else if(sessionUser instanceof Comprador){
                redirect(controller: 'app', action: 'comprador')
            }else if(sessionUser instanceof Estoquista){
                redirect(controller: 'app', action: 'estoquista')
            }else if(sessionUser instanceof Administrador){
                redirect(controller: 'admin')
            }else{
                redirect uri: SpringSecurityUtils.securityConfig.successHandler.defaultTargetUrl
            }
            return
        }
        else {
            redirect action: auth, params: params
        }
    }

    /**
     * Show the login page.
     */
    def auth = {
        def config = SpringSecurityUtils.securityConfig
        if (springSecurityService.isLoggedIn()) {
            def sessionUser = usuarioService.sessionUser
            if (sessionUser.isNewUser()){
                redirect(controller: 'login', action: 'changePass', params: [password: params.password])
            }else if(sessionUser instanceof Representante){
                redirect(controller: 'app', action: 'representante')
            }else if(sessionUser instanceof Comprador){
                redirect(controller: 'app', action: 'comprador')
            }else if(sessionUser instanceof Estoquista){
                redirect(controller: 'app', action: 'estoquista')
            }else if(sessionUser instanceof Administrador){
                redirect(controller: 'admin')
            }else{
                config.successHandler.defaultTargetUrl
            }
            return
        }


        render(view: "../index", model:
                [postUrl: "${request.contextPath}${config.apf.filterProcessesUrl}",
                rememberMeParameter: config.rememberMe.parameter])
    }

    def doLogin = {
        def out = usuarioService.doLogin(params.email, params.password, params.rememberMe)

        if(!(out instanceof Usuario)){
            flash.error = out
            params.remove("password")
        }
        redirect action:auth, params:params
    }


    /**
     * Show denied page.
     */
    def denied = {
        if (springSecurityService.isLoggedIn() &&
                authenticationTrustResolver.isRememberMe(SCH.context?.authentication)) {
            // have cookie but the page is guarded with IS_AUTHENTICATED_FULLY
            redirect action: full, params: params
        }
    }

    /**
     * Login page for users with a remember-me cookie but accessing a IS_AUTHENTICATED_FULLY page.
     */

    def full = {
        def config = SpringSecurityUtils.securityConfig
        render view: '../index', params: params,
                model: [hasCookie: authenticationTrustResolver.isRememberMe(SCH.context?.authentication),
                        postUrl: "${request.contextPath}${config.apf.filterProcessesUrl}"]
    }
    /**
     * Callback after a failed login. Redirects to the auth page with a warning message.
     */

    def authfail = {

        def username = session[UsernamePasswordAuthenticationFilter.SPRING_SECURITY_LAST_USERNAME_KEY]
        String msg = ''
        def exception = session[AbstractAuthenticationProcessingFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY]
        if (exception) {
            if (exception instanceof AccountExpiredException) {
                msg = SpringSecurityUtils.securityConfig.errors.login.expired
            }
            else if (exception instanceof CredentialsExpiredException) {
                msg = SpringSecurityUtils.securityConfig.errors.login.passwordExpired
            }
            else if (exception instanceof DisabledException) {
                msg = SpringSecurityUtils.securityConfig.errors.login.disabled
            }
            else if (exception instanceof LockedException) {
                msg = SpringSecurityUtils.securityConfig.errors.login.locked
            }
            else {
                msg = SpringSecurityUtils.securityConfig.errors.login.fail
            }
        }

        if (springSecurityService.isAjax(request)) {
            render([error: msg] as JSON)
        }
        else {
            flash.message = msg
            redirect action: auth, params: params
        }
    }
    /**
     * The Ajax success redirect url.
     */

    def ajaxSuccess = {
        render([success: true, username: springSecurityService.authentication.name] as JSON)
    }
    /**
     * The Ajax denied redirect url.
     */


    def ajaxDenied = {
        render([error: 'access denied'] as JSON)
    }


    private void nocache(response) {
        response.setHeader('Cache-Control', 'no-cache') // HTTP 1.1
        response.addDateHeader('Expires', 0)
        response.setDateHeader('max-age', 0)
        response.setIntHeader ('Expires', -1) //prevents caching at the proxy server
        response.addHeader('cache-Control', 'private') //IE5.x only
    }

    def saveNewPassword = {
        def result
        if(params?.novaSenha == params?.novaSenha2){
            result = usuarioService.mudeSenha(params.email, params.senha, params.novaSenha)
            def user = usuarioService.sessionUser
            user.newUser = false
            usuarioService.saveUsuario(user)
        }else{
            result = "Senhas digitadas não são iguais, tente novamente!"
        }
        if(result instanceof Usuario)                   {
            flash.error = ""
            flash.message = "Senha alterada com sucesso"
            redirect(action:'index')
        }else{
            flash.message = ""
            flash.error = result
            render view:'changePass'
        }
    }

    def changePass = {
        render view: 'changePass', model: [email: usuarioService.sessionUser.email, password: params.password]
    }

    def forgotPassword = {
        render view: 'forgotPassword'
    }

    def sendRedefinePasswordEmail = {
        def user = Usuario.findByEmail(params?.email)
        if (!user){
            flash.error = "Nenhuma conta encontrada para o E-mail fornecido"
        }else{
            def registrationCode = RegistrationCode.findByUsername(user.email)
            if (!registrationCode)
                registrationCode = new RegistrationCode(username: user.email)
            if (registrationCode.validate() && registrationCode.save(flush: true)) {
                flash.message = "Instruções para redefinição de senha enviadas para ${params?.email}!"
                notifierService.notifiqueRecuperacaoDeSenha(registrationCode, user)
            }else{
                flash.error = registrationCode.errors
            }
        }
        redirect (action: 'forgotPassword')
    }

    def changeLostPassword = {
        def registrationCode = RegistrationCode.findByToken(params?.token)
        if (registrationCode){
            def usuario = Usuario.findByEmail(registrationCode.username)
            if(params?.password == params?.password1){
                usuario.password = springSecurityService.encodePassword(params?.password)
                if (!usuario.hasErrors() && usuario.save())      {
                    flash.message = "Senha alterada com sucesso!"
                    registrationCode.delete()
                    redirect(action: 'auth')
                }
            }else{
                flash.error = "As senhas digitadas não são iguais, tente novamente!"
            }
        }else{
            flash.error = "Não existe requisição de recuperação de senha para esse usuário, sua solicitação pode ter expirado, tente solicitar novamente!"
        }
        params?.password = ""
        params?.password1 = ""
        render (view: 'createNewPassword', params: params)
    }

    def createNewPassword = {
        [token: params?.token]
    }

}
