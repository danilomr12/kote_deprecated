
<%@ page import="br.com.cotecom.domain.pedido.Pedido" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main" />
    <title>Editar Pedido</title>
</head>
<body>
<content tag="navbuttons">
    <li>
        <g:link action="list">Listar Pedidos</g:link>
    </li>
</content>

<div>
    <h1 class="page-header">Editar Pedido</h1>
    <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${pedidoInstance}">
        <div class="errors">
            <g:renderErrors bean="${pedidoInstance}" as="list" />
        </div>
    </g:hasErrors>

    <g:form role="form" class="col-md-6" method="post" >
    <input type="hidden" name="id" value="${pedidoInstance?.id}" />
    <input type="hidden" name="version" value="${pedidoInstance?.version}" />
    
    <div class="form-group ${hasErrors(bean:pedidoInstance,field:'dataCriacao','has-error')}">
        <label>Data Criacao:</label>
        <p class="form-control-static" > <g:formatDate date="${pedidoInstance.dataCriacao}" format="dd-MM-yyyy HH:mm:ss" /></p>
    </div>


        <div class="form-group ${hasErrors(bean:pedidoInstance,field:'resposta','has-error')}">
            <label>Resposta:</label>
            <p class="form-control-static" >
                <g:link controller="respostaAdmin" action="show" id="${pedidoInstance.resposta.id}">
                    ${pedidoInstance?.resposta}
                </g:link>
            </p>
        </div>



        <div class="form-group ${hasErrors(bean:pedidoInstance,field:'itens','has-error')}">
        <label >Itens:</label>
        
<ul>
<g:each var="i" in="${pedidoInstance?.itens?}">
    <li>${i?.encodeAsHTML()}</li>
</g:each>
</ul>
    </div>

    
    <div class="form-group ${hasErrors(bean:pedidoInstance,field:'faturado','has-error')}">
        <label for="faturado">Faturado:</label>
        <g:checkBox class="form-control" name="faturado" value="${pedidoInstance?.faturado}" ></g:checkBox>
    </div>

    <g:actionSubmit action="update" class="save btn btn-default" value="Atualizar" />
    <g:actionSubmit action="delete" class="delete btn btn-default" onclick="return confirm('Are you sure?');" value="Deletear" />
    </g:form>
</div>
</body>
</html>
