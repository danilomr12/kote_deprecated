<%@ page import="br.com.cotecom.domain.usuarios.Comprador; br.com.cotecom.domain.usuarios.Estoquista; br.com.cotecom.domain.usuarios.empresa.Cliente" %>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main" />
    <title>Cliente</title>
</head>
<body>
<content tag="navbuttons">
    <li>
        <g:link action="create">Nova Empresa</g:link>
    </li>
    <li>
        <g:link action="list">Listar Empresa</g:link>
    </li>
</content>

<div class="body">
    <h1 class="page-header">Cliente</h1>

    <g:form controller="empresaAdmin" action="showResumoCompras" role="form" class="col-md-7" >

        <div class="form-group">
            <label>id:</label>
            <p class="form-control-static">${empresaInstance?.id}</p>
        </div>

        <div class="form-group">
            <label>Nome Fantasia:</label>
            <p class="form-control-static">${empresaInstance?.nomeFantasia}</p>
        </div>

        <div class="form-group">
            <label>Razão social:</label>
            <p class="form-control-static">${empresaInstance?.razaoSocial}</p>
        </div>

        <div class="form-group">
            <label>CNPJ:</label>
            <p class="form-control-static">${empresaInstance?.cnpj}</p>
        </div>

        <div class="form-group">
            <label>Telefones:</label>

            <p class="form-control-static">
                <ul>
        <g:each var="t" in="${empresaInstance?.telefones}">
            <li>${t?.encodeAsHTML()}</li>
        </g:each>
        </ul>
            </p>
        </div>
        <div class="form-group">
            <label>Email:</label>
            <p class="form-control-static">${empresaInstance?.email}</p>
        </div>

        <p class="sub-header">
            Ver total de compras por período
        </p>
        <div class="form-group">
            <label>Data início:</label>
            <p class="form-control">
                <g:datePicker name="dataInicio" noSelection="['':'-esolha data de início-']" precision="day" value="${dataInicio}"/>
            </p>
        </div>

        <div class="form-group">
            <label>Data Fim:</label>
            <p class="form-control">
                <g:datePicker name="dataFim" noSelection="['':'-esolha data de fim-']" precision="day" value="${dataFim}"/>
            </p>
        </div>
        <g:if test="{totalDasComprasNoMes!=null}">
            <div class="form-group">
                <label>Valor total no período selecionado:</label>
                <p class="form-control-static">
            <g:formatNumber  type="currency" number="${totalDasComprasNoMes}"/>
            <g:if test="${flash.message}">
                <div class="message">${flash.message}</div>
            </g:if>
            </p>
        </div>
        </g:if>

        <div class="buttons">
            <input type="hidden" name="id" value="${empresaInstance?.id}" />
            <button type="submit" class="btn btn-default">Buscar</button>
        </div>
    </g:form>
</div>
</body>
</html>
