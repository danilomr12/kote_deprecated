
<%@ page import="br.com.cotecom.domain.usuarios.Usuario; br.com.cotecom.domain.usuarios.empresa.Empresa" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main" />
    <title>Editar Empresa</title>
</head>
<body>
<content tag="navbuttons">
    <li>
        <g:link action="list">Listar Empresas</g:link>
    </li>
    <li>
        <g:link action="create">Novo Empresa</g:link>
    </li>
</content>

<div>
    <h1 class="page-header">Editar Empresa</h1>
    <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${empresaInstance}">
        <div class="errors">
            <g:renderErrors bean="${empresaInstance}" as="list" />
        </div>
    </g:hasErrors>

    <g:form role="form" class="col-md-5" method="post" >
    <input type="hidden" name="id" value="${empresaInstance?.id}" />
    <input type="hidden" name="version" value="${empresaInstance?.version}" />
    
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

    
    <div class="form-group ${hasErrors(bean:empresaInstance,field:'ramo','has-error')}">
        <label for="ramo">Ramo:</label>
        
    </div>

    
    <div class="form-group ${hasErrors(bean:empresaInstance,field:'categorias','has-error')}">
        <label for="categorias">Categorias:</label>
        <g:select class="form-control" name="categorias"
from="${br.com.cotecom.domain.usuarios.empresa.Categoria.list()}"
size="5" multiple="yes" optionKey="id"
value="${empresaInstance?.categorias}" />

    </div>

    
    <div class="form-group ${hasErrors(bean:empresaInstance,field:'telefones','has-error')}">
        <label for="telefones">Telefones:</label>
        <g:select class="form-control" name="telefones"
from="${br.com.cotecom.domain.usuarios.empresa.Telefone.list()}"
size="5" multiple="yes" optionKey="id"
value="${empresaInstance?.telefones}" />

    </div>


    
    <div class="form-group ${hasErrors(bean:empresaInstance,field:'email','has-error')}">
        <label for="email">Email:</label>
        <input type="text" id="email" class="form-control" name="email" value="${fieldValue(bean:empresaInstance,field:'email')}"/>
    </div>

    
    <div class="form-group ${hasErrors(bean:empresaInstance,field:'usuarios','has-error')}">
        <label for="usuarios">Usuarios:</label>
        
<ul>
<g:each var="u" in="${empresaInstance?.usuarios?}">
    <li><g:link controller="usuarioAdmin" action="show" id="${u.id}">${u?.encodeAsHTML()}</g:link></li>
</g:each>
</ul>
<g:link controller="usuarioAdmin" params="['empresa.id':empresaInstance?.id, 'tipo': Usuario.REPRESENTANTE]" action="create">Add Usuario</g:link>

    </div>

    
    <div class="form-group ${hasErrors(bean:empresaInstance,field:'tipo','has-error')}">
        <label for="tipo">Tipo:</label>
        <input type="text" class="form-control" name="tipo" id="tipo" value="${fieldValue(bean:empresaInstance,field:'tipo')}" />
    </div>

    

    <g:actionSubmit action="update" class="save btn btn-default" value="Atualizar" />
    <g:actionSubmit action="delete" class="delete btn btn-default" onclick="return confirm('Are you sure?');" value="Deletear" />
    </g:form>
</div>
</body>
</html>
