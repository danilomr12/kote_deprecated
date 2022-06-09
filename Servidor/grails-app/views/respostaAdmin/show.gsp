
<%@ page import="br.com.cotecom.domain.resposta.EstadoResposta; br.com.cotecom.domain.resposta.Resposta" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main" />
    <title>Mostar Resposta</title>
</head>
<body>
<content tag="navbuttons">
    <li>
        <g:link class="list" action="list">Listar Resposta</g:link>
    </li>

</content>

<h1 class="page-header">Mostrar Resposta</h1>
<g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
</g:if>
<div class="col-md-9">
    <table class="table table-bordered table-striped">
        <tbody>

        
        <tr class="prop">
            <td valign="top" class="name">Id:</td>
            
            <td valign="top" class="value">${fieldValue(bean:respostaInstance, field:'id')}</td>
            
        </tr>
        
        <tr class="prop">
            <td valign="top" class="name">Representante:</td>
            
            <td valign="top" class="value"><g:link controller="usuarioAdmin" action="show" id="${respostaInstance?.representante?.id}">${respostaInstance?.representante?.encodeAsHTML()}</g:link></td>
            
        </tr>
        
        <tr class="prop">
            <td valign="top" class="name">Codigo Estado:</td>
            
            <td valign="top" class="value">${fieldValue(bean:respostaInstance, field:'codigoEstado')}</td>
            
        </tr>

        <tr class="prop">
            <td valign="top" class="name">Estado:</td>

            <td valign="top" class="value">${EstadoResposta.descricao[respostaInstance.codigoEstado]}</td>

        </tr>


        <tr class="prop">
            <td valign="top" class="name">Pedidos:</td>
            
            <td  valign="top" style="text-align:left;" class="value">
                <ul>
                    <g:each var="p" in="${respostaInstance.pedidos}">
                        <li><g:link controller="pedidoAdmin" action="show" id="${p.id}">${p?.toString()}</g:link></li>
                    </g:each>
                </ul>
            </td>
            
        </tr>
        
        <tr class="prop">
            <td valign="top" class="name">Cotacao:</td>
            
            <td valign="top" class="value"><g:link controller="cotacaoAdmin" action="show" id="${respostaInstance?.cotacao?.id}">${respostaInstance?.cotacao?.encodeAsHTML()}</g:link></td>
            
        </tr>
        
        <tr class="prop">
            <td valign="top" class="name">Data Criacao:</td>
            
            <td valign="top" class="value"><g:formatDate date="${respostaInstance.dataCriacao}" format="dd-MM-yyyy HH:mm:ss" /></td>
            
        </tr>
        
        <tr class="prop">
            <td valign="top" class="name">Data Salva:</td>
            
            <td valign="top" class="value"><g:formatDate date="${respostaInstance.dataSalva}" format="dd-MM-yyyy HH:mm:ss" /></td>
            
        </tr>

        <tr class="prop">
            <td valign="top" class="name">Lida:</td>

            <td valign="top" class="value">${fieldValue(bean:respostaInstance, field:'lida')}</td>

        </tr>



        <tr class="prop">
            <td valign="top" class="name">Itens:</td>
            
            <td  valign="top" style="text-align:left;" class="value">
                <ul>
                    <g:each var="i" in="${respostaInstance.itens}">
                        <li>${i?.encodeAsHTML()}</li>
                    </g:each>
                </ul>
            </td>
            
        </tr>

        </tbody>
    </table>
    <g:form>
        <input type="hidden" name="id" value="${respostaInstance?.id}" />
        <g:actionSubmit class="edit btn btn-default" action="edit" value="Editar" />
        <g:actionSubmit class="delete btn btn-default" action="delete" onclick="return confirm('Tem certeza?');" value="Deletar" />
    </g:form>
</div>
</body>
</html>
