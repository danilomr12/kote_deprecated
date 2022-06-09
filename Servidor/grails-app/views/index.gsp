<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<meta name="layout" content="login"/>
<content tag="middle">
    <div class="content clearfix">

        <g:form action='doLogin' method='POST' name="login" controller="login" id='loginForm' useToken="true">

            <h1>Acesse sua Conta</h1>

            <div class="login-fields">

                <p>Faça login com sua conta registrada:</p>
                <br />
                <g:if test='${flash.error}'>
                    <h5 class='alert-error'>${flash.error}</h5>
                    <br/>
                </g:if>
                <g:if test='${flash.message}'>
                    <h5 class='alert-success'>${flash.message}</h5>
                    <br/>
                </g:if>

                <div class="field">
                    <label for="username">E-mail:</label>
                    <input type="text" id="username" name="email" value="${params?.email}" placeholder="E-mail" class="login username-field" />
                </div> <!-- /field -->

                <div class="field">
                    <label for="password">Senha:</label>
                    <input type="password" id="password" name="password" value="" placeholder="Senha" class="login password-field"/>
                </div> <!-- /password -->

            </div> <!-- /login-fields -->

            <div class="login-actions">

                <label class="checkbox">
                    <input type="checkbox">Mantenha-me conectado
                </label>

                <br />
                <button class="button btn btn-danger">Entrar</button>

            </div> <!-- .actions -->
        </g:form>

    </div> <!-- /content -->

    <div>
        <small>
            Não possui uma conta? <g:link url="http://www.kote.com.br/solicite.html">Solicite aqui</g:link><br />
            Esqueceu a senha? <g:link controller="login" action="forgotPassword">Clique aqui</g:link>
        </small>
    </div>
</content>

</html>