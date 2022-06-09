<html>
<head>
    <meta name="layout" content="login"/>
    <script type="text/javascript">
        function verifique(event) {
            if(document.getElementById('checkbox-termo-uso').checked == false){
                alert("É necessário aceitar os termos de uso e política de privacidade");
                return false
            }  else{
                document.changePassForm.submit()
                return true
            }
        }
    </script>
</head>
<content tag="middle">

    <div class="content clearfix">

        <g:form action='saveNewPassword' method='POST' controller="login" id="changePassForm" useToken="true">

            <h1>Acesse sua Conta</h1>

            <div class="login-fields">

                <p>Digite seus dados para alterar sua senha:</p>
                <br />
                <g:if test='${flash.error}'>
                    <div class='login_message'>${flash.error}</div>
                    <br/>
                </g:if>
                <g:if test='${flash.message}'>
                    <div class='success_message'>${flash.message}</div>
                    <br/>
                </g:if>
                <div class="field">
                    <label for="username">E-mail:</label>
                    <input type="text" id="username" name="email" value="${email}" placeholder="E-mail" class="login username-field"/>
                </div> <!-- /field -->

                <div class="field">
                    <label for="password">Senha Atual:</label>
                    <input type="password" id="password" name="senha" value="${password}" placeholder="Senha Atual" class="login password-field"/>
                </div> <!-- /password -->

                <div class="field">
                    <label for="new-password">Nova senha:</label>
                    <input type="password" id="new-password" name="novaSenha" value="" placeholder="Nova Senha" class="login password-field"/>
                </div> <!-- /new password -->

                <div class="field">
                    <label for="new-password2">Nova senha:</label>
                    <input type="password" id="new-password2" name="novaSenha2" value="" placeholder="Repita a Nova Senha" class="login password-field"/>
                </div> <!-- /new password -->

            </div> <!-- /login-fields -->

            <div class="login-actions">

                <label class="checkbox">
                    <input id="checkbox-termo-uso" type="checkbox">Certifico que li e concordo com os <g:link url="http://www.kote.com.br/termos.html">
                    Termos de uso </g:link> e <g:link url="http://www.kote.com.br/privacidade.html">Política de privacidade</g:link>
                e em receber comunicações eletrônicas relacionadas à minha conta no<g:link url="http://www.kote.com.br">
                    KOTE</g:link>
                </label>

                <br />
                <button class="button btn btn-danger" onclick="return verifique(event)">Entrar</button>

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