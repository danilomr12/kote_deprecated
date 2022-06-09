
<%@ page import="br.com.cotecom.domain.resposta.EstadoResposta; br.com.cotecom.domain.resposta.Resposta" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main" />
    <title>Listar Resposta</title>
</head>
<body>

<content tag="navbuttons">
    <g:form class="navbar-form navbar-right" action="buscar">
        <g:textField class="form-control" placeholder="Buscar Resposta" name="busca" title="buscar Resposta" style="width: 300px" />
        <g:submitButton name="Buscar" class="btn btn-default"/>
    </g:form>
</content>


<h1 class="page-header">Listar Resposta</h1>
<g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
</g:if>

<table class="table table-hover">
    <thead>
    <tr>
        
        <g:sortableColumn property="id" title="Id" />
        
        <th>Representante</th>
        
        <g:sortableColumn property="codigoEstado" title="Codigo Estado" />
        
        <td>Estado</td>
        
        <th>Pedidos</th>
        
        <th>Cotacao</th>
        
    </tr>
    </thead>
    <tbody>
    <g:each in="${respostaInstanceList}" status="i" var="respostaInstance">
        <tr>
            
            <td><g:link action="show" id="${respostaInstance.id}">${fieldValue(bean:respostaInstance, field:'id')}</g:link></td>
            
            <td>${fieldValue(bean:respostaInstance, field:'representante')}</td>
            
            <td>${fieldValue(bean:respostaInstance, field:'codigoEstado')}</td>

            <td>${EstadoResposta.descricao[respostaInstance.codigoEstado]}</td>
            
            <td>${fieldValue(bean:respostaInstance, field:'pedidos')}</td>
            
            <td><g:link action="show" controller="cotacaoAdmin" id="${respostaInstance?.cotacao?.id}" >${fieldValue(bean:respostaInstance, field:'cotacao')}</g:link></td>
            
        </tr>
    </g:each>
    </tbody>
</table>
<div class="paginateButtons">
    <g:paginate total="${respostaInstanceTotal}" params="${[busca: busca]}"/>
</div>
</body>
</html>
