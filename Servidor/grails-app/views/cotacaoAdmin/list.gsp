
<%@ page import="br.com.cotecom.domain.cotacao.EstadoCotacao; br.com.cotecom.domain.cotacao.Cotacao" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main" />
    <title>Listar Cotacao</title>
</head>
<body>

<content tag="navbuttons">
    <li>
        <g:link class="create" action="create">Novo Cotacao</g:link>
    </li>
    <g:form class="navbar-form navbar-right" action="buscar">
        <g:textField class="form-control" placeholder="Buscar Cotacao" name="busca" title="buscar Cotacao" style="width: 300px" />
        <g:submitButton name="Buscar" class="btn btn-default"/>
    </g:form>
</content>


<h1 class="page-header">Listar Cotacao</h1>
<g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
</g:if>

<table class="table table-hover">
    <thead>
    <tr>
        
        <g:sortableColumn property="id" title="Id" />

        <g:sortableColumn property="empresa.nomeFantasia" title="Cliente" />

        <g:sortableColumn property="codigoEstadoCotacao" title="Estado" />

        <th>Número de itens</th>

        <g:sortableColumn property="prazoPagamento" title="Prazo de pagamento" />

        <g:sortableColumn property="titulo" title="Titulo" />

        <g:sortableColumn property="dataCriacao" title="Data Criacao" />
        
        <g:sortableColumn property="dataEntrega" title="Data Entrega" />
        
        <g:sortableColumn property="dataValidade" title="Data Validade" />
        
        <g:sortableColumn property="dataSalva" title="Data Salva" />
        
    </tr>
    </thead>
    <tbody>
    <g:each in="${cotacaoInstanceList}" status="i" var="cotacaoInstance">
        <tr>
            
            <td><g:link action="show" id="${cotacaoInstance.id}">${fieldValue(bean:cotacaoInstance, field:'id')}</g:link></td>

            <td><g:link controller="empresaAdmin" action="show" id="${cotacaoInstance?.empresa?.id}">${fieldValue(bean:cotacaoInstance, field:'empresa.nomeFantasia')}</g:link></td>

            <td>${EstadoCotacao.descricao[cotacaoInstance?.codigoEstadoCotacao]}</td>

            <td>${cotacaoInstance?.itens?.size()}</td>

            <td>${fieldValue(bean:cotacaoInstance, field:'prazoPagamento')}</td>

            <td>${fieldValue(bean:cotacaoInstance, field:'titulo')}</td>

            <td><g:formatDate date="${cotacaoInstance?.dataCriacao}" format="dd-MM-yyyy HH:mm:ss" /></td>

            <td><g:formatDate date="${cotacaoInstance?.dataEntrega}" format="dd-MM-yyyy HH:mm:ss" /></td>

            <td><g:formatDate date="${cotacaoInstance.dataValidade}" format="dd-MM-yyyy HH:mm:ss" /></td>

            <td><g:formatDate date="${cotacaoInstance.dataSalva}" format="dd-MM-yyyy HH:mm:ss" /></td>

        </tr>
    </g:each>
    </tbody>
</table>
<div class="paginateButtons">
    <g:paginate total="${cotacaoInstanceTotal}" params="${[busca: busca]}"/>
</div>
</body>
</html>
