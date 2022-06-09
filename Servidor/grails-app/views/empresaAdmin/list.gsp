
<%@ page import="br.com.cotecom.domain.usuarios.empresa.Empresa" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main" />
    <title>Listar Empresa</title>
</head>
<body>

<content tag="navbuttons">
    <li>
        <g:link class="create" action="create">Novo Empresa</g:link>
    </li>
    <g:form class="navbar-form navbar-right" action="buscar">
        <g:textField class="form-control" placeholder="Buscar Empresa" name="busca" title="buscar Empresa" style="width: 300px" />
        <g:submitButton name="Buscar" class="btn btn-default"/>
    </g:form>
</content>


<h1 class="page-header">Listar Empresa</h1>
<g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
</g:if>

<table class="table table-hover">
    <thead>
    <tr>
        
        <g:sortableColumn property="id" title="Id" />
        
        <g:sortableColumn property="nomeFantasia" title="Nome Fantasia" />
        
        <g:sortableColumn property="razaoSocial" title="Razao Social" />
        
        <g:sortableColumn property="cnpj" title="Cnpj" />
        
        <th>Tipo</th>
        
        <th>Endereco</th>
        
    </tr>
    </thead>
    <tbody>
    <g:each in="${empresaInstanceList}" status="i" var="empresaInstance">
        <tr>
            
            <td><g:link action="show" id="${empresaInstance.id}">${fieldValue(bean:empresaInstance, field:'id')}</g:link></td>
            
            <td>${fieldValue(bean:empresaInstance, field:'nomeFantasia')}</td>
            
            <td>${fieldValue(bean:empresaInstance, field:'razaoSocial')}</td>
            
            <td>${fieldValue(bean:empresaInstance, field:'cnpj')}</td>
            
            <td>${fieldValue(bean:empresaInstance, field:'tipo')}</td>
            
            <td>${fieldValue(bean:empresaInstance, field:'endereco')}</td>
            
        </tr>
    </g:each>
    </tbody>
</table>
<div class="paginateButtons">
    <g:paginate total="${empresaInstanceTotal}" params="${[busca: busca]}"/>
</div>
</body>
</html>
