
<%@ page import="br.com.cotecom.domain.cotacao.EstadoCotacao; br.com.cotecom.domain.cotacao.Cotacao" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main" />
    <title>Create Cotacao</title>
</head>
<body>
<content tag="navbuttons">
    <li>
        <g:link class="list" action="list">Listar Cotacao</g:link>
    </li>
</content>

<div>
    <h1>Criar Cotacao</h1>
    <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${cotacaoInstance}">
        <div class="errors">
            <g:renderErrors bean="${cotacaoInstance}" as="list" />
        </div>
    </g:hasErrors>
    <g:form action="save" method="post" role="form" class="col-md-6" >
        
        <div class="form-group ${hasErrors(bean:cotacaoInstance,field:'titulo','has-error')}">
            <label for="titulo">Titulo:</label>
            <input type="text" id="titulo" class="form-control" name="titulo" value="${fieldValue(bean:cotacaoInstance,field:'titulo')}"/>
        </div>


        
        <div class="form-group ${hasErrors(bean:cotacaoInstance,field:'dataCriacao','has-error')}">
            <label for="dataCriacao">Data Criacao:</label>
            <g:datePicker class="form-control" name="dataCriacao" value="${cotacaoInstance?.dataCriacao}" precision="minute" ></g:datePicker>
        </div>


        
        <div class="form-group ${hasErrors(bean:cotacaoInstance,field:'dataEntrega','has-error')}">
            <label for="dataEntrega">Data Entrega:</label>
            <g:datePicker class="form-control" name="dataEntrega" value="${cotacaoInstance?.dataEntrega}" precision="minute" noSelection="['':'']"></g:datePicker>
        </div>


        
        <div class="form-group ${hasErrors(bean:cotacaoInstance,field:'dataValidade','has-error')}">
            <label for="dataValidade">Data Validade:</label>
            <g:datePicker class="form-control" name="dataValidade" value="${cotacaoInstance?.dataValidade}" precision="minute" noSelection="['':'']"></g:datePicker>
        </div>


        
        <div class="form-group ${hasErrors(bean:cotacaoInstance,field:'dataSalva','has-error')}">
            <label for="dataSalva">Data Salva:</label>
            <g:datePicker class="form-control" name="dataSalva" value="${cotacaoInstance?.dataSalva}" precision="minute" ></g:datePicker>
        </div>


        
        <div class="form-group ${hasErrors(bean:cotacaoInstance,field:'prazoPagamento','has-error')}">
            <label for="prazoPagamento">Prazo Pagamento:</label>
            <input type="text" id="prazoPagamento" class="form-control" name="prazoPagamento" value="${fieldValue(bean:cotacaoInstance,field:'prazoPagamento')}"/>
        </div>


        
        <div class="form-group ${hasErrors(bean:cotacaoInstance,field:'empresa','has-error')}">
            <label for="empresa">Empresa:</label>
            <g:select optionKey="id" from="${br.com.cotecom.domain.usuarios.empresa.Cliente.list()}" class="form-control" name="empresa.id" value="${cotacaoInstance?.empresa?.id}" ></g:select>
        </div>


        
        <div class="form-group ${hasErrors(bean:cotacaoInstance,field:'enderecoEntrega','has-error')}">
            <label for="enderecoEntrega">Endereco Entrega:</label>
            <g:select optionKey="id" from="${br.com.cotecom.domain.usuarios.empresa.Endereco.list()}" class="form-control" name="enderecoEntrega.id" value="${cotacaoInstance?.enderecoEntrega?.id}" noSelection="['null':'']"></g:select>
        </div>


        
        <div class="form-group ${hasErrors(bean:cotacaoInstance,field:'codigoEstadoCotacao','has-error')}">
            <label for="codigoEstadoCotacao">Codigo Estado Cotacao:</label>
            <g:select id="codigoEstadoCotacao" class="form-control" name="codigoEstadoCotacao" value="${fieldValue(bean:cotacaoInstance,field:'codigoEstadoCotacao')}" optionValue="value" optionKey="key" from="${[0:EstadoCotacao.descricao[0].value]}"/>
        </div>


        
        <div class="form-group ${hasErrors(bean:cotacaoInstance,field:'mensagem','has-error')}">
            <label for="mensagem">Mensagem:</label>
            <input type="text" id="mensagem" class="form-control" name="mensagem" value="${fieldValue(bean:cotacaoInstance,field:'mensagem')}"/>
        </div>


        <button type="submit" class="btn btn-default">Salvar</button>
    </g:form>
</div>
</body>
</html>
