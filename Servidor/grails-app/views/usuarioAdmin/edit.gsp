<%@ page import="br.com.cotecom.domain.usuarios.Comprador; br.com.cotecom.domain.usuarios.Estoquista; br.com.cotecom.domain.usuarios.empresa.Cliente; br.com.cotecom.domain.usuarios.Administrador; br.com.cotecom.domain.usuarios.empresa.CoteCom; br.com.cotecom.domain.usuarios.empresa.Fornecedor; br.com.cotecom.domain.usuarios.Usuario" %>
<%
    def empresaList
%>
<%def list = [Usuario.ADMINISTRADOR, Usuario.REPRESENTANTE, Usuario.SUPERVISOR, Usuario.ESTOQUISTA, Usuario.COMPRADOR]%>
<g:if test="${usuarioInstance instanceof Comprador || usuarioInstance instanceof Estoquista}">
    <%empresaList = Cliente.list()%>
</g:if>
<g:elseif test="${usuarioInstance instanceof Administrador}">
    <%empresaList = CoteCom.list()%>
</g:elseif>
<g:else>
    <%empresaList = Fornecedor.list()%>
</g:else>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Editar Usuario</title>
    </head>
    <body>
    <content tag="navbuttons">
        <li>
            <g:link action="list">Listar Usuário</g:link>
        </li>
        <li>
            <g:link action="create">Novo Usuário</g:link>
        </li>
    </content>

    <div>
            <h1 class="page-header">Editar Usuario</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${usuarioInstance}">
            <div class="errors">
                <g:renderErrors bean="${usuarioInstance}" as="list" />
            </div>
            </g:hasErrors>

            <g:form role="form" class="col-md-5" method="post">
                <input type="hidden" name="id" value="${usuarioInstance?.id}" />
                <input type="hidden" name="version" value="${usuarioInstance?.version}" />

                <div class="form-group">
                    <label class="control-label ">Tipo:</label>
                    <div>
                        <p class="form-control-static">${usuarioInstance.tipo} </p>
                    </div>
                </div>

                <div class="form-group">
                    <label for="email">Email:</label>
                    <input class="form-control" type="text" id="email" name="email" value="${fieldValue(bean:usuarioInstance,field:'email')}"/>
                </div>

                <div class="form-group">
                    <label for="nome">Nome:</label>
                    <input type="text" id="nome" name="nome" class="form-control" value="${fieldValue(bean:usuarioInstance,field:'nome')}"/>
                </div>
                <div class="form-group">
                    <label for="password">Password:</label>
                    <input type="password" maxlength="255" id="password" name="password" class="form-control" value="${fieldValue(bean:usuarioInstance,field:'password')}"/>
                </div>
                <div class="form-group">
                    <label for="empresa.id">Empresa:</label>
                    <g:select id="empresas" optionValue="nomeFantasia" optionKey="id" from="${empresaList}" name="empresa.id" class="form-control" value="${usuarioInstance?.empresa?.id}" noSelection="['null':'']"/>
                </div>

                <div class="form-group">
                <label for="telefones">Telefones:</label>
                <g:select name="telefones" from="${br.com.cotecom.domain.usuarios.empresa.Telefone.list()}" size="5" multiple="yes" optionKey="id" value="${usuarioInstance?.telefones}" />
                </div>
                <div class="form-group">
                    <label for="accountExpired">Account Expired:</label>
                    <g:checkBox name="accountExpired" class="form-control" value="${usuarioInstance?.accountExpired}" ></g:checkBox>
                </div>
                <div class="form-group">
                    <label for="accountLocked">Account Locked:</label>
                    <g:checkBox name="accountLocked" class="form-control" value="${usuarioInstance?.accountLocked}" ></g:checkBox>
                </div>
                <div class="form-group">
                    <label for="habilitado">Habilitado:</label>
                    <g:checkBox name="habilitado" class="form-control" value="${usuarioInstance?.habilitado}" ></g:checkBox>
                </div>
                <div class="form-group">
                    <label for="isDeletado">Is Deletado:</label>
                    <g:checkBox name="isDeletado" class="form-control" value="${usuarioInstance?.isDeletado}" ></g:checkBox>
                </div>
                <div class="form-group">
                    <label for="newUser">New User:</label>
                    <g:checkBox name="newUser" class="form-control" value="${usuarioInstance?.newUser}" ></g:checkBox>
                </div>
                <div class="form-group">
                    <label for="passwordExpired">Password Expired:</label>
                    <g:checkBox name="passwordExpired" class="form-control" value="${usuarioInstance?.passwordExpired}" ></g:checkBox>
                </div>


                <g:actionSubmit action="update" class="save btn btn-default" value="Atualizar" />
                <g:actionSubmit action="delete" class="delete btn btn-default" onclick="return confirm('Are you sure?');" value="Deletear" />
            </g:form>
        </div>
    </body>
</html>
