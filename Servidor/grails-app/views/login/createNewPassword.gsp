<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="login"/>
</head>
<content tag="middle">

    <div class="content clearfix">

        <g:form action='changeLostPassword' method='POST' controller="login" id="changePassForm" useToken="true">

            <h1>Recuperação de senha</h1>

            <div class="login-fields">

                <p>Digite sua nova senha e a repita logo abaixo:</p>
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
                    <label for="password">Nova senha:</label>
                    <input type="password" id="password" name="password" value="" placeholder="Nova senha" class="login password-field" />
                </div> <!-- /field -->

                <div class="field">
                    <label for="new-password"> Repita sua senha:</label>
                    <input type="password" id="new-password" name="password1" value="" placeholder="Repita sua senha" class="login password-field"/>
                </div> <!-- /password -->
            <g:hiddenField name="token" value="${token}"/>
            </div> <!-- /login-fields -->

            <div class="login-actions">
                <br />
                <button class="button btn btn-danger">Enviar</button>

            </div> <!-- .actions -->

        </g:form>

    </div> <!-- /content -->

</content>
</html>