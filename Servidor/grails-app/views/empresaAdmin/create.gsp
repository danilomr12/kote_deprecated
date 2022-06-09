<%@ page import="br.com.cotecom.domain.usuarios.empresa.Endereco; br.com.cotecom.domain.usuarios.empresa.Empresa" %>
<%
    def tipoList = [Empresa.FORNECEDOR, Empresa.CLIENTE, Empresa.KOTE]
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main" />
    <title>Create Empresa</title>
</head>
<body>
<content tag="navbuttons">
    <li>
        <g:link class="list" action="list">Listar Empresa</g:link>
    </li>
</content>

    <h1 class="page-header">Criar Empresa</h1>
    <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${empresaInstance}">
        <div class="errors">
            <g:renderErrors bean="${empresaInstance}" as="list" />
        </div>
    </g:hasErrors>
    <g:form action="save" method="post" role="form" class="col-md-5" >
        
        <div class="form-group ${hasErrors(bean:empresaInstance,field:'nomeFantasia','has-error')}">
            <label for="nomeFantasia">Nome Fantasia:</label>
            <input type="text" id="nomeFantasia" class="form-control" name="nomeFantasia" value="${fieldValue(bean:empresaInstance,field:'nomeFantasia')}"/>
        </div>


        
        <div class="form-group ${hasErrors(bean:empresaInstance,field:'razaoSocial','has-error')}">
            <label for="razaoSocial">Razao Social:</label>
            <input type="text" id="razaoSocial" class="form-control" name="razaoSocial" value="${fieldValue(bean:empresaInstance,field:'razaoSocial')}"/>
        </div>


        
        <div class="form-group ${hasErrors(bean:empresaInstance,field:'cnpj','has-error')}">
            <label for="cnpj">Cnpj:</label>
            <input type="text" id="cnpj" class="form-control" name="cnpj" value="${fieldValue(bean:empresaInstance,field:'cnpj')}"/>
        </div>

        <div class="form-group ${hasErrors(bean:empresaInstance,field:'email','has-error')}">
            <label for="email">Email:</label>
            <input type="text" id="email" class="form-control" name="email" value="${fieldValue(bean:empresaInstance,field:'email')}"/>
        </div>


        
        <div class="form-group ${hasErrors(bean:empresaInstance,field:'tipo','has-error')}">
            <label for="tipo">Tipo:</label>
            <g:select from="${tipoList}" class="form-control" name="tipo" value="" noSelection="['null':'']"/>
        </div>


        

        <button type="submit" class="btn btn-default">Salvar</button>
    </g:form>
</body>
</html>