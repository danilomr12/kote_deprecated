<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<meta name="layout" content="login"/>

<content tag="middle">

    <div class="content clearfix">

        <g:if test='${flash.message}'>
            <div class='success_message'>${flash.message}</div>
            <br/>
        </g:if>
        <g:else>
            <g:form action='sendRedefinePasswordEmail' method='POST' name="login" controller="login" id='loginForm' useToken="true">


                <h1>Esqueceu sua senha?</h1>

                <div class="login-fields">
                    <p>Digite seu e-mail abaixo, um e-mail contendo instruções para redefinir sua senha será enviado.</p>
                    <br />

                    <g:if test='${flash.error}'>
                        <div class='login_message'>${flash.error}</div>
                        <br/>
                    </g:if>


                    <div class="field">
                        <label for="username">E-mail:</label>
                        <input type="text" id="username" name="email" value="${params?.email}" placeholder="E-mail" class="login username-field" />
                    </div> <!-- /field -->


                </div> <!-- /login-fields -->

                <div class="login-actions">

                    <br />
                    <button class="button btn btn-danger">Enviar</button>

                </div> <!-- .actions -->
            </g:form>
        </g:else>
    </div> <!-- /content -->

</content>
</html>

