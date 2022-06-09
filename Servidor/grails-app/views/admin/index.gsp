<%@ page import="br.com.cotecom.domain.usuarios.empresa.Cliente; br.com.cotecom.services.UsuarioService; br.com.cotecom.domain.usuarios.Comprador" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main" />

    <title>Administrador KOTE</title>

    <sec:ifSwitched>
        <a href='${request.contextPath}/j_spring_security_exit_user'>
            Resume as <sec:switchedUserOriginalUsername/>
        </a>
    </sec:ifSwitched>
    <b><g:link uri="/logout">sair</g:link></b>
</head>

<body>
<g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
</g:if>

<h1 class="page-header"><g:sayHelloToUser/>, seja bem vindo!</h1>
<p>
    Escolha a opção na barra lateral esquerda ou uma opção administrativa abaixo.
</p>

<h4 class="sub-header">Gerar cobranças do mês</h4>

<g:if test="${flash.message}">
    <div class="alert alert-info">${flash.message}</div>
</g:if>
<p class="sub-header">
    Escolha o perído para gerar o relatório de compras
</p>
<g:form controller="empresaAdmin" action="gerarCobrancasDeTodosClientes" role="form" class="form-horizontal" >

    <div class="form-group">
        <label class="col-sm-2 control-label">Data início:</label>
        <p class="col-sm-6">
            <g:datePicker name="dataInicio" noSelection="['':'-esolha data de início-']" precision="day" value="${dataInicio?dataInicio:new Date()}"/>
        </p>
    </div>

    <div class="form-group">
        <label class="control-label col-sm-2">Data Fim:</label>
        <p class="col-sm-6">
            <g:datePicker class="form-control" name="dataFim" noSelection="['':'-esolha data de fim-']" precision="day" value="${dataFim?dataFim:(new Date())}"/>
        </p>
    </div>

    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <button type="submit" class="btn btn-default">Buscar</button>
        </div>
    </div>

    <g:if test="${result!=null}">
        <div class="form-group alert alert-info ">
            <label>Valor total no período selecionado:</label>
        <p class="form-control-static">
            <g:each in="${result}" var="item">
                Empresa: ${Cliente.read(item.key as Long)}: <g:formatNumber  type="currency" number="${item.value}"/>
                </p>
            </g:each>
        </div>
    </g:if>

</g:form>

<h4 class="sub-header">Monitoramento java melody</h4>


<g:link uri="/monitoring">Abrir monitoramento java-melody</g:link>

</body>
</html>