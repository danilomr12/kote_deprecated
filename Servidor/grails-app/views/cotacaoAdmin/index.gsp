<%@ page import="br.com.cotecom.domain.usuarios.empresa.Cliente" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main" />

    <title>Cotação admin</title>

</head>

<body>
<content tag="cotacaooactive">
    active
</content>

<content tag="navbuttons">
    <li>
        <g:link class="list" action="list">Listar Cotacao</g:link>
    </li>
    <li>
        <g:link action="create">Nova Cotacao</g:link>
    </li>
</content>

<h1 class="page-header">Cotação</h1>

<h4 class="sub-header">Crie cotação rascunho a partir de planilha</h4>

<g:form action="crieCotacaoRascunhoAPartirDeExcel" controller="test" method="post" role="form" class="form-horizontal"
        enctype="multipart/form-data">

    <div class="form-group">
        <label class="col-sm-2 control-label" >Baixar planilha de exemplo</label>
        <div class="col-sm-6">
            <g:link action="downloadPlanilhaExemplo" class="btn-default btn" role="button" controller="cotacaoAdmin">Planilha de Exemplo</g:link>
        </div>
    </div>

    <div class="form-group">
        <label class="col-sm-2 control-label" for="planilha">Planilha a enviar</label>
        <div class="col-sm-6">
            <input id="planilha" type="file" name="planilha" class="form-control"/>
        </div>
    </div>

    <div class="form-group">
        <label class="col-sm-2 control-label" for="planilha">Empresa</label>
        <div class="col-sm-6">
            <g:select from="${Cliente.list()}" noSelection="['':'-Escolha a empresa-']"
                      optionValue="nomeFantasia" optionKey="id" name="empresaId" class="form-control"/>
        </div>
    </div>

    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <button type="submit" class="btn btn-default">Enviar</button>
        </div>
    </div>

</g:form>


</body>
</html>