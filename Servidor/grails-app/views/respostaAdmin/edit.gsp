
<%@ page import="br.com.cotecom.domain.resposta.Resposta" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main" />
    <title>Editar Resposta</title>
</head>
<body>
<content tag="navbuttons">
    <li>
        <g:link action="list">Listar Respostas</g:link>
    </li>
</content>

<div>
    <h1 class="page-header">Editar Resposta</h1>
    <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${respostaInstance}">
        <div class="errors">
            <g:renderErrors bean="${respostaInstance}" as="list" />
        </div>
    </g:hasErrors>

    <g:form role="form" class="col-md-9" method="post" >
    <input type="hidden" name="id" value="${respostaInstance?.id}" />
    <input type="hidden" name="version" value="${respostaInstance?.version}" />
    
    <div class="form-group ${hasErrors(bean:respostaInstance,field:'representante','has-error')}">
        <label>Representante:</label>
        <p class="form-control-static"><g:link action="show" controller="usuarioAdmin" id="${respostaInstance?.representante?.id}">${respostaInstance?.representante}</g:link></p>
    </div>

    
    <div class="form-group ${hasErrors(bean:respostaInstance,field:'codigoEstado','has-error')}">
        <label for="codigoEstado">Codigo Estado:</label>
        <input type="text" id="codigoEstado" class="form-control" name="codigoEstado" value="${fieldValue(bean:respostaInstance,field:'codigoEstado')}" />
    </div>

    
    <div class="form-group ${hasErrors(bean:respostaInstance,field:'pedidos','has-error')}">
        <label for="pedidos">Pedidos:</label>
        
<ul>
<g:each var="p" in="${respostaInstance?.pedidos?}">
    <li><g:link controller="pedidoAdmin" action="show" id="${p.id}">${p?.encodeAsHTML()}</g:link></li>
</g:each>
</ul>

    </div>

    
    <div class="form-group ${hasErrors(bean:respostaInstance,field:'cotacao','has-error')}">
        <label >Cotacao:</label>
        <p class="form-control-static"><g:link action="show" controller="cotacaoAdmin" id="${respostaInstance?.cotacao?.id}">${respostaInstance?.cotacao}</g:link></p>
    </div>

    
    <div class="form-group ${hasErrors(bean:respostaInstance,field:'dataCriacao','has-error')}">
        <label for="dataCriacao">Data Criacao:</label>
        <g:datePicker class="form-control" name="dataCriacao" value="${respostaInstance?.dataCriacao}" precision="minute" ></g:datePicker>
    </div>

    
    <div class="form-group ${hasErrors(bean:respostaInstance,field:'dataSalva','has-error')}">
        <label for="dataSalva">Data Salva:</label>
        <g:datePicker class="form-control" name="dataSalva" value="${respostaInstance?.dataSalva}" precision="minute" ></g:datePicker>
    </div>

    
    <div class="form-group ${hasErrors(bean:respostaInstance,field:'itens','has-error')}">
        <label for="itens">Itens:</label>
        
<ul>
<g:each var="i" in="${respostaInstance?.itens?}">
    <li>${i?.encodeAsHTML()}</li>
</g:each>
</ul>

    </div>

    
    <div class="form-group ${hasErrors(bean:respostaInstance,field:'lida','has-error')}">
        <label for="lida">Lida:</label>
        <g:checkBox class="form-control" name="lida" value="${respostaInstance?.lida}" ></g:checkBox>
    </div>

    <g:actionSubmit action="update" class="save btn btn-default" value="Atualizar" />
    <g:actionSubmit action="delete" class="delete btn btn-default" onclick="return confirm('Are you sure?');" value="Deletear" />
    </g:form>
</div>
</body>
</html>
