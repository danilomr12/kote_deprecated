<%@ page import="br.com.cotecom.domain.usuarios.empresa.Empresa" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <title>Mostar Empresa</title>
</head>

<body>
<content tag="navbuttons">
    <li>
        <g:link class="list" action="list">Listar Empresa</g:link>
    </li>
    <li>
        <g:link class="create" action="create">Novo Empresa</g:link>
    </li>

</content>

<h1 class="page-header">Mostrar Empresa</h1>
<g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
</g:if>
<div class="col-md-6">
    <table class="table table-bordered table-striped">
        <tbody>

        <tr class="prop">
            <td valign="top" class="name">Id:</td>

            <td valign="top" class="value">${fieldValue(bean: empresaInstance, field: 'id')}</td>

        </tr>

        <tr class="prop">
            <td valign="top" class="name">Nome Fantasia:</td>

            <td valign="top" class="value">${fieldValue(bean: empresaInstance, field: 'nomeFantasia')}</td>

        </tr>

        <tr class="prop">
            <td valign="top" class="name">Razao Social:</td>

            <td valign="top" class="value">${fieldValue(bean: empresaInstance, field: 'razaoSocial')}</td>

        </tr>

        <tr class="prop">
            <td valign="top" class="name">Cnpj:</td>

            <td valign="top" class="value">${fieldValue(bean: empresaInstance, field: 'cnpj')}</td>

        </tr>

        <tr class="prop">
            <td valign="top" class="name">Ramo:</td>

            <td valign="top" class="value">${fieldValue(bean: empresaInstance, field: 'ramo')}</td>

        </tr>

        <tr class="prop">
            <td valign="top" class="name">Categorias:</td>

            <td valign="top" style="text-align:left;" class="value">
                <ul>
                    <g:each var="c" in="${empresaInstance.categorias}">
                        <li><g:link controller="categoria" action="show" id="${c.id}">${c?.encodeAsHTML()}</g:link></li>
                    </g:each>
                </ul>
            </td>

        </tr>

        <tr class="prop">
            <td valign="top" class="name">Telefones:</td>

            <td valign="top" style="text-align:left;" class="value">
                <ul>
                    <g:each var="t" in="${empresaInstance.telefones}">
                        <li><g:link controller="telefone" action="show" id="${t.id}">${t?.encodeAsHTML()}</g:link></li>
                    </g:each>
                </ul>
            </td>

        </tr>

        <tr class="prop">
            <td valign="top" class="name">Endereco:</td>

            <td valign="top" class="value">${empresaInstance?.endereco?.encodeAsHTML()}</td>

        </tr>

        <tr class="prop">
            <td valign="top" class="name">Email:</td>

            <td valign="top" class="value">${fieldValue(bean: empresaInstance, field: 'email')}</td>

        </tr>

        <tr class="prop">
            <td valign="top" class="name">Usuarios:</td>

            <td valign="top" style="text-align:left;" class="value">
                <ul>
                    <g:each var="u" in="${empresaInstance.usuarios}">
                        <li><g:link controller="usuarioAdmin" action="show" id="${u.id}">${u?.encodeAsHTML()}</g:link></li>
                    </g:each>
                </ul>
            </td>

        </tr>

        <tr class="prop">
            <td valign="top" class="name">Tipo:</td>

            <td valign="top" class="value">${fieldValue(bean: empresaInstance, field: 'tipo')}</td>

        </tr>

        <g:if test="${empresaInstance.tipo==Empresa.CLIENTE}">
            <tr class="prop" >
                <td valign="top" class="name">Ver total de compras por período</td>
                <td valign="top" style="text-align: left" class="value">
                    <g:link controller="empresaAdmin" action="showResumoCompras" id="${empresaInstance?.id}">Ir para cobrança</g:link>
                </td>
            </tr>


        </g:if>
        </tbody>
    </table>
    <g:form>
        <input type="hidden" name="id" value="${empresaInstance?.id}"/>
        <g:actionSubmit class="edit btn btn-default" action="edit" value="Editar"/>
        <g:actionSubmit class="delete btn btn-default" action="delete" onclick="return confirm('Tem certeza?');"
                        value="Deletar"/>
    </g:form>
</div>
</body>
</html>
