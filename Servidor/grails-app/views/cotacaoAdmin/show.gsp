
<%@ page import="br.com.cotecom.domain.cotacao.EstadoCotacao; br.com.cotecom.domain.cotacao.Cotacao" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main" />
    <title>Mostar Cotacao</title>
</head>
<body>
<content tag="navbuttons">
    <li>
        <g:link class="list" action="list">Listar Cotacao</g:link>
    </li>
    <li>
        <g:link class="create" action="create">Novo Cotacao</g:link>
    </li>

</content>

<h1 class="page-header">Mostrar Cotacao</h1>
<g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
</g:if>
<div class="col-md-8">
    <table class="table table-bordered table-striped">
        <tbody>

        
        <tr class="prop">
            <td valign="top" class="name">Id:</td>
            
            <td valign="top" class="value">${fieldValue(bean:cotacaoInstance, field:'id')}</td>
            
        </tr>
        
        <tr class="prop">
            <td valign="top" class="name">Titulo:</td>
            
            <td valign="top" class="value">${fieldValue(bean:cotacaoInstance, field:'titulo')}</td>
            
        </tr>
        
        <tr class="prop">
            <td valign="top" class="name">Data Criacao:</td>
            
            <td valign="top" class="value"><g:formatDate date="${cotacaoInstance.dataCriacao}" format="dd-MM-yyyy HH:mm:ss" /></td>
            
        </tr>
        
        <tr class="prop">
            <td valign="top" class="name">Data Entrega:</td>
            
            <td valign="top" class="value"><g:formatDate date="${cotacaoInstance.dataEntrega}" format="dd-MM-yyyy HH:mm:ss" /></td>
            
        </tr>
        
        <tr class="prop">
            <td valign="top" class="name">Data Validade:</td>
            
            <td valign="top" class="value"><g:formatDate date="${cotacaoInstance.dataValidade}" format="dd-MM-yyyy HH:mm:ss" /></td>
            
        </tr>
        
        <tr class="prop">
            <td valign="top" class="name">Data Salva:</td>
            
            <td valign="top" class="value"><g:formatDate date="${cotacaoInstance.dataSalva}" format="dd-MM-yyyy HH:mm:ss" /></td>
            
        </tr>
        
        <tr class="prop">
            <td valign="top" class="name">Prazo Pagamento:</td>
            
            <td valign="top" class="value">${fieldValue(bean:cotacaoInstance, field:'prazoPagamento')}</td>
            
        </tr>
        
        <tr class="prop">
            <td valign="top" class="name">Empresa:</td>
            
            <td valign="top" class="value"><g:link controller="empresaAdmin" action="show" id="${cotacaoInstance?.empresa?.id}">${cotacaoInstance?.empresa?.toString()}</g:link></td>
            
        </tr>
        
        <tr class="prop">
            <td valign="top" class="name">Endereco Entrega:</td>
            
            <td valign="top" class="value"><g:link controller="endereco" action="show" id="${cotacaoInstance?.enderecoEntrega?.id}">${cotacaoInstance?.enderecoEntrega?.encodeAsHTML()}</g:link></td>
            
        </tr>
        
        <tr class="prop">
            <td valign="top" class="name">Codigo Estado Cotacao:</td>
            
            <td valign="top" class="value">${fieldValue(bean:cotacaoInstance, field:'codigoEstadoCotacao')}</td>
            
        </tr>

        <tr class="prop">
            <td valign="top" class="name">Estado:</td>

            <td valign="top" class="value">${EstadoCotacao.descricao[cotacaoInstance.codigoEstadoCotacao]}</td>

        </tr>
        
        <tr class="prop">
            <td valign="top" class="name">Mensagem:</td>
            
            <td valign="top" class="value">${fieldValue(bean:cotacaoInstance, field:'mensagem')}</td>
            
        </tr>
        
        <tr class="prop">
            <td valign="top" class="name">Compra Id:</td>
            
            <td valign="top" class="value">${fieldValue(bean:cotacaoInstance, field:'compraId')}</td>
            
        </tr>
        
        <tr class="prop">
            <td valign="top" class="name">Itens:</td>
            
            <td  valign="top" style="text-align:left;" class="value">
                <ul>
                    <g:each var="i" in="${cotacaoInstance.itens}">
                        <li>${i?.encodeAsHTML()}</li>
                    </g:each>
                </ul>
            </td>
            
        </tr>
        
        <tr class="prop">
            <td valign="top" class="name">Respostas:</td>
            
            <td  valign="top" style="text-align:left;" class="value">
                <ul>
                    <g:each var="r" in="${cotacaoInstance.respostas}">
                        <li><g:link controller="respostaAdmin" action="show" id="${r.id}">${r?.encodeAsHTML()}</g:link></li>
                    </g:each>
                </ul>
            </td>
            
        </tr>
        
        </tbody>
    </table>
    <g:form>
        <input type="hidden" name="id" value="${cotacaoInstance?.id}" />
        <g:actionSubmit class="edit btn btn-default" action="edit" value="Editar" />
        <g:actionSubmit class="delete btn btn-default" action="delete" onclick="return confirm('Tem certeza?');" value="Deletar" />
    </g:form>
</div>
</body>
</html>
