<%@ page import="br.com.cotecom.domain.usuarios.Usuario" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main" />
    <title>Mostrar usuário</title>
</head>
<body>
<content tag="navbuttons">
    <li>
        <g:link class="list" action="list">Listar Usuário</g:link>
    </li>
    <li>
        <g:link class="create" action="create">Novo usuário</g:link>
    </li>

</content>

<h1 class="page-header">Mostrar usuário</h1>
<g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
</g:if>
<div class="col-md-6">
    <table class="table table-bordered table-striped">
        <tbody>
        <tr class="prop">
            <td valign="top" class="name">Id:</td>

            <td valign="top" class="value">${fieldValue(bean:usuarioInstance, field:'id')}</td>

        </tr>

        <tr class="prop">
            <td valign="top" class="name">Email:</td>

            <td valign="top" class="value">${fieldValue(bean:usuarioInstance, field:'email')}</td>

        </tr>

        <tr class="prop">
            <td valign="top" class="name">Nome:</td>

            <td valign="top" class="value">${fieldValue(bean:usuarioInstance, field:'nome')}</td>

        </tr>

        <tr class="prop">
            <td valign="top" class="name">Empresa:</td>

            <td valign="top" class="value"><g:link controller="empresaAdmin" action="show" id="${usuarioInstance?.empresa?.id}">${usuarioInstance.empresa?usuarioInstance?.empresa?.tipo +" - "+usuarioInstance?.empresa?.nomeFantasia:""}</g:link></td>

        </tr>

        <tr class="prop">
            <td valign="top" class="name">Account Expired:</td>

            <td valign="top" class="value">${fieldValue(bean:usuarioInstance, field:'accountExpired')}</td>

        </tr>

        <tr class="prop">
            <td valign="top" class="name">Account Locked:</td>

            <td valign="top" class="value">${fieldValue(bean:usuarioInstance, field:'accountLocked')}</td>

        </tr>

        <tr class="prop">
            <td valign="top" class="name">Habilitado:</td>

            <td valign="top" class="value">${fieldValue(bean:usuarioInstance, field:'habilitado')}</td>

        </tr>

        <tr class="prop">
            <td valign="top" class="name">Is Deletado:</td>

            <td valign="top" class="value">${fieldValue(bean:usuarioInstance, field:'isDeletado')}</td>

        </tr>

        <tr class="prop">
            <td valign="top" class="name">New User:</td>

            <td valign="top" class="value">${fieldValue(bean:usuarioInstance, field:'newUser')}</td>

        </tr>

        <tr class="prop">
            <td valign="top" class="name">Password Expired:</td>

            <td valign="top" class="value">${fieldValue(bean:usuarioInstance, field:'passwordExpired')}</td>

        </tr>

        <tr class="prop">
            <td valign="top" class="name">Telefones:</td>

            <td  valign="top" style="text-align:left;" class="value">
                <ul>
                    <g:each var="t" in="${usuarioInstance.telefones}">
                        <li><g:link controller="telefone" action="show" id="${t.id}">${t?.encodeAsHTML()}</g:link></li>
                    </g:each>
                </ul>
            </td>

        </tr>

        <tr class="prop">
            <td valign="top" class="name">Responsabilidades:</td>

            <td valign="top" class="value">
                <ul>
                    <g:each in="${usuarioInstance?.responsabilidades}" var="responsabilidade">
                        <li>
                            ${responsabilidade.descricao}
                        </li>
                    </g:each>
                </ul>
            </td>
        </tr>

        <tr class="prop">
            <td valign="top" class="name">Subordinados:</td>

            <td valign="top" class="value">
                <ul>
                    <g:each in="${usuarioInstance?.subordinados}" var="subordinado">
                        <li>
                            ${subordinado.nome}
                        </li>
                    </g:each>
                </ul>
            </td>

        </tr>

        <tr class="prop">
            <td valign="top" class="name">Supervisor:</td>

            <td valign="top" class="value">${fieldValue(bean:usuarioInstance, field:'supervisor')}</td>

        </tr>
        <tr class="prop">
            <td valign="top" class="name"> Logar como este Usuário</td>
            <td valign="top" class="value">
                <sec:ifAllGranted roles='ROLE_ADMIN'>
                    <form action='${request.contextPath}/j_spring_security_switch_user' method='POST'>
                        <input class="btn btn-default" type='submit' value='Switch'/>
                        <g:hiddenField type='text' name='j_username' value="${usuarioInstance?.email}"/><br/>
                    </form>
                </sec:ifAllGranted>
            </td>
        </tr>

        </tbody>
    </table>
    <g:form>
        <input type="hidden" name="id" value="${usuarioInstance?.id}" />
        <g:actionSubmit class="edit btn btn-default" action="edit" value="Editar" />
        <g:actionSubmit class="delete btn btn-default" action="delete" onclick="return confirm('Are you sure?');" value="Deletar" />
    </g:form>
</div>
</body>
</html>
