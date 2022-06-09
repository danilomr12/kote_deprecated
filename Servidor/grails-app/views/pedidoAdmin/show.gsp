
<%@ page import="br.com.cotecom.domain.pedido.Pedido" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main" />
    <title>Mostar Pedido</title>
</head>
<body>
<content tag="navbuttons">
    <li>
        <g:link class="list" action="list">Listar Pedido</g:link>
    </li>
</content>

<h1 class="page-header">Mostrar Pedido</h1>
<g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
</g:if>
<div class="col-md-8">
    <table class="table table-bordered table-striped">
        <tbody>

        
        <tr class="prop">
            <td valign="top" class="name">Id:</td>
            
            <td valign="top" class="value">${fieldValue(bean:pedidoInstance, field:'id')}</td>
            
        </tr>
        
        <tr class="prop">
            <td valign="top" class="name">Data Criacao:</td>
            
            <td valign="top" class="value">
                <g:formatDate date="${pedidoInstance.dataCriacao}" format="dd-MM-yyyy HH:mm:ss" /></td>
            
        </tr>

        <tr class="prop">
            <td valign="top" class="name">Pedido Url Download:</td>
            
            <td valign="top" class="value">
                <g:link controller="pedido" action="exportePedidoExcel" params="${[digest: pedidoInstance.pedidoUrlDigest]}"> ${fieldValue(bean:pedidoInstance, field:'pedidoUrlDigest')}</g:link>
                </td>
            
        </tr>
        
        <tr class="prop">
            <td valign="top" class="name">Faturado:</td>
            
            <td valign="top" class="value">${fieldValue(bean:pedidoInstance, field:'faturado')}</td>
            
        </tr>
        
        <tr class="prop">
            <td valign="top" class="name">Resposta:</td>
            
            <td valign="top" class="value"><g:link controller="respostaAdmin" action="show" id="${pedidoInstance?.resposta?.id}">${pedidoInstance?.resposta?.encodeAsHTML()}</g:link></td>
            
        </tr>

        <tr class="prop">
            <td valign="top" class="name">Reenvie e-mail pedido:</td>

            <td valign="top" class="value"><g:link controller="pedidoAdmin" action="reenviePedido" params="[pedidoId: pedidoInstance?.id]">Enviar e-mail</g:link>
            </td>

        </tr>

        <tr class="prop">
            <td valign="top" class="name">Itens:</td>

            <td  valign="top" style="text-align:left;" class="value">
                <ul>
                    <g:each var="i" in="${pedidoInstance.itens}">
                        <li>${i?.encodeAsHTML()}</li>
                    </g:each>
                </ul>
            </td>

        </tr>

        </tbody>
    </table>
    <g:form>
        <input type="hidden" name="id" value="${pedidoInstance?.id}" />
        <g:actionSubmit class="edit btn btn-default" action="edit" value="Editar" />
        <g:actionSubmit class="delete btn btn-default" action="delete" onclick="return confirm('Tem certeza?');" value="Deletar" />
    </g:form>
</div>
</body>
</html>
