<%@ page import="br.com.cotecom.domain.usuarios.Usuario" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main" />
    <title>Listar Usuarios</title>
</head>
<body>

<content tag="navbuttons">
    <li>
        <g:link class="create" action="create">Novo usuário</g:link>
    </li>
    <g:form class="navbar-form navbar-right" action="buscar">
        <g:textField class="form-control" placeholder="Buscar usuário" name="busca" title="busca usuario" style="width: 300px" />
        <g:submitButton name="Buscar" class="btn btn-default"/>
    </g:form>
</content>

<h1 class="page-header">Listar Usuario</h1>
<g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
</g:if>

<table class="table table-hover">
    <thead>
    <tr>

        <g:sortableColumn property="id" title="Id" />

        <g:sortableColumn property="email" title="Email" />

        <g:sortableColumn property="nome" title="Nome" />

        <g:sortableColumn property="empresa.nomeFantasia" title="Empresa"/>

        <th>Tipo</th>

        <g:sortableColumn property="accountExpired" title="Account Expired"/>

        <g:sortableColumn property="habilitado" title="Hablitado"/>

        <g:sortableColumn property="accountLocked" title="AccountLocked"/>

        <g:sortableColumn property="accountLocked" title="passwordExpired"/>

        <g:sortableColumn property="newUser" title="New User"/>

        <g:sortableColumn property="IsDeletado" title="IsDeletado"/>

    </tr>
    </thead>
    <tbody>
    <g:each in="${usuarioInstanceList}" status="i" var="usuarioInstance">
        <tr>

            <td><g:link action="show" id="${usuarioInstance.id}">${fieldValue(bean:usuarioInstance, field:'id')}</g:link></td>

            <td>${fieldValue(bean:usuarioInstance, field:'email')}</td>

            <td>${fieldValue(bean:usuarioInstance, field:'nome')}</td>

            <td><g:link action="show" controller="empresaAdmin" id="${usuarioInstance?.empresa?.id}" >${usuarioInstance.empresa.toString()} </g:link></td>

            <td>${usuarioInstance?.tipo}</td>

            <td>${fieldValue(bean:usuarioInstance, field:'accountExpired')}</td>

            <td>${fieldValue(bean:usuarioInstance, field:'habilitado')}</td>

            <td>${fieldValue(bean:usuarioInstance, field:'accountLocked')}</td>

            <td>${fieldValue(bean:usuarioInstance, field:'passwordExpired')}</td>

            <td>${fieldValue(bean:usuarioInstance, field:'newUser')}</td>

            <td>${fieldValue(bean:usuarioInstance, field:'isDeletado')}</td>


        </tr>
    </g:each>
    </tbody>
</table>
<div class="paginateButtons">
    <g:paginate total="${usuarioInstanceTotal}" params="${[busca: busca]}"/>
</div>
</body>
</html>
