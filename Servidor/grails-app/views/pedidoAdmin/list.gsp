
<%@ page import="br.com.cotecom.domain.pedido.Pedido" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main" />
    <title>Listar Pedido</title>
</head>
<body>

<content tag="navbuttons">
</content>


<h1 class="page-header">Listar Pedido</h1>
<g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
</g:if>

<table class="table table-hover">
    <thead>
    <tr>
        
        <g:sortableColumn property="id" title="Id" />
        
        <g:sortableColumn property="dataCriacao" title="Data Criacao" />
        
        <g:sortableColumn property="pedidoUrlDigest" title="Pedido Excel download" />

        <g:sortableColumn property="faturado" title="Faturado" />
        
        <th>Resposta</th>
        
    </tr>
    </thead>
    <tbody>
    <g:each in="${pedidoInstanceList}" status="i" var="pedidoInstance">
        <tr>
            
            <td><g:link action="show" id="${pedidoInstance.id}">${fieldValue(bean:pedidoInstance, field:'id')}</g:link></td>
            
            <td><g:formatDate date="${pedidoInstance.dataCriacao}" format="dd-MM-yyyy HH:mm:ss" /></td>
            
            <td><g:link controller="pedido" action="exportePedidoExcel" params="${[digest: pedidoInstance.pedidoUrlDigest]}"> ${fieldValue(bean:pedidoInstance, field:'pedidoUrlDigest')}</g:link></td>
            
            <td>${fieldValue(bean:pedidoInstance, field:'faturado')}</td>
            
            <td><g:link controller="respostaAdmin" action="show" id="${pedidoInstance?.resposta?.id}">${fieldValue(bean:pedidoInstance, field:'resposta')}</g:link> </td>
            
        </tr>
    </g:each>
    </tbody>
</table>
<div class="paginateButtons">
    <g:paginate total="${pedidoInstanceTotal}" params="${[busca: busca]}"/>
</div>
</body>
</html>
