<%@ page import="br.com.cotecom.domain.usuarios.empresa.Cliente" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main" />
    <title>Catálogo</title>
</head>
<body>

<content tag="catalogoactive">
    active
</content>

<content tag="navbuttons">
    <li>
        <g:link class="create" action="create">Novo Produto</g:link>
    </li>
</content>

<g:if test="${flash.message}">
    <div class="alert alert-info">${flash.message}</div>
</g:if>

<h1 class="page-header">Catálogo</h1>

<h4 class="sub-header">Importar produtos de planilha excel</h4>

<g:form controller="import" action="enviar" method="post" role="form" class="form-horizontal"
        enctype="multipart/form-data">

    <div class="form-group">
        <label class="col-sm-2 control-label" >Baixar planilha de exemplo</label>
        <div class="col-sm-6">
            <g:link action="downloadPlanilhaExemplo" controller="import">Planilha de Exemplo</g:link>
        </div>
    </div>

    <div class="form-group">
        <label class="col-sm-2 control-label" for="arquivo">Planilha a enviar</label>
        <div class="col-sm-6">
            <input id="arquivo" type="file" name="arquivo" class="form-control"/>
        </div>
    </div>

    <div class="form-group">
        <label class="col-sm-2 control-label" for="arquivo">Cliente</label>
        <div class="col-sm-6">
          <g:select class="form-control" name="empresa" optionKey="id" from="${Cliente.list()}" />
        </div>
    </div>

    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <button type="submit" class="btn btn-default">Enviar</button>
        </div>
    </div>
</g:form>

<h4 class="sub-header">Reindexar produtos do catalogo</h4>

<g:link controller="catalogoAdmin" action="reindexCatalogo">Reindexar produtos do catalogo</g:link>

</body>
</html>