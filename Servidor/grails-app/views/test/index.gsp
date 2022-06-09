<%@ page import="br.com.cotecom.domain.cotacao.EstadoCotacao; br.com.cotecom.domain.cotacao.Cotacao; br.com.cotecom.domain.usuarios.empresa.Cliente; br.com.cotecom.domain.usuarios.Comprador" contentType="text/html;charset=UTF-8" %>
<html>
<head><title>Teste Controller</title>
    <meta name="layout" content="main" >
</head>
<body>
<content tag="testactive">
    active
</content>

<g:if test="${flash.message}">
    <div class="alert alert-info">${flash.message}</div>
</g:if>

<h1 class="page-header">Test controller</h1>

<h4>Limpar dados de cotações do usuario demo</h4>

<g:link controller="test" action="limparDadosDemoUser">Apagar dados</g:link>

<h4 class="sub-header">Mocks produtos</h4>
<div class="form-horizontal">
    <div class="form-group">
        <label class="col-sm-4 control-label" >Importe 7 mil produtos de teste Jc distribuição</label>
        <div class="col-sm-6">
            <g:link controller="test" action="importeProdutosDeTeste" class="btn btn-default" role="button">Importar</g:link>
        </div>
    </div>

    <div class="form-group">
        <label class="col-sm-4 control-label" >
            Teste Insert Produto
        </label>
        <div class="col-sm-6">
            <g:link controller="test" action="insertProduto"
                    class="btn btn-default" role="button">
                Insert
            </g:link>
        </div>
    </div>

</div>
<h4 class="sub-header">Mocks usuários</h4>
<div class="form-horizontal">
    <div class="form-group">
        <label class="col-sm-4 control-label" >
            Crie 4 representantes com fornecedor sem atendimento
        </label>
        <div class="col-sm-6">
            <g:link controller="test" action="crieRepresentantes"
                    class="btn btn-default" role="button">
                Criar
            </g:link>
        </div>
    </div>
</div>

<h4 class="sub-header">Mocks cotação</h4>
<div class="form-horizontal">
    <div class="form-group">
        <label class="col-sm-4 control-label" >
            Crie cotação enviada para 4 representantes
        </label>
        <div class="col-sm-6">
            <g:link controller="test" action="crieCotacaoEnviadaParaQuatroRepresentantes"
                    class="btn btn-default" role="button">
                Criar
            </g:link>
        </div>
    </div>

    <hr>

    <g:form action="preenchaRespostasCotacao" method="post" enctype="multipart/form-data" >
        <div class="form-group">
            <label class="col-sm-4 control-label" >
                Preencha Respostas da Cotacao. Id da cotação:
            </label>
            <div class="col-sm-4" >
                <g:textField type="text" name="id" class="form-control"/>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-4 col-sm-10">
                <button type="submit" class="btn btn-default">Enviar</button>
            </div>
        </div>
    </g:form>
    <hr>
    <div class="form-group">
        <label class="col-sm-4 control-label" >
            Teste download Arquivo
        </label>
        <div class="col-sm-6">
            <g:link controller="test" action="testDownloadFile"
                    class="btn btn-default" role="button">
                Download
            </g:link>
        </div>
    </div>

    <hr>
    <g:form action="crieCotacaoAPartirDeExcelEEnvieA10Reps" method="post" enctype="multipart/form-data">

        <div class="form-group">
            <label class="col-sm-4 control-label" >
                Crie cotação a partir de planilha e envie a 10 representantes
            </label>
            <div class="col-sm-4" >
                <input type="file" name="planilha" class="form-control"/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label" >
                Comprador
            </label>
            <div class="col-sm-4" >
                <g:select from="${Comprador.list()}" noSelection="['':'-Escolha o comprador-']"
                          optionValue="nome" optionKey="id" name="compradorId" class="form-control"/>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-4 col-sm-10">
                <button type="submit" class="btn btn-default">Enviar</button>
            </div>
        </div>
    </g:form>

    <hr>

    <g:form action="crieCotacaoAPartirDeAnaliseExcelEnvieAosRepresentantes" method="post" enctype="multipart/form-data">
        <div class="form-group">
            <label class="col-sm-4 control-label" >
                Crie cotação a partir de planilha de analise e envie
            </label>
            <div class="col-sm-4" >
                <input type="file" name="planilha" class="form-control"/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label" >
                Comprador
            </label>
            <div class="col-sm-4" >
                <g:select from="${Cliente.list()}" noSelection="['':'-Escolha a empresa-']"
                          optionValue="razaoSocial" optionKey="id" name="empresaId" class="form-control"/>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-4 col-sm-10">
                <button type="submit" class="btn btn-default">Enviar</button>
            </div>
        </div>
    </g:form>

    <hr>

    <g:form action="respondaCotacaoAPartirDaAnalise" method="post" enctype="multipart/form-data">
        <div class="form-group">
            <label class="col-sm-4 control-label" >
                Responda cotação a partir de planilha de analise e envie
            </label>
            <div class="col-sm-4" >
                <input type="file" name="planilha" class="form-control"/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label" >
                Comprador
            </label>
            <div class="col-sm-4" >
                <g:select from="${Cotacao.listOrderById(order: 'desc')}" noSelection="['':'-Escolha a Cotacao-']"
                          optionValue="id" optionKey="id" name="cotacaoId" class="form-control"/>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-4 col-sm-10">
                <button type="submit" class="btn btn-default">Enviar</button>
            </div>
        </div>
    </g:form>

    <hr>

    <g:form controller="test" action="calculeEconomiaMediaDaCotacao" name="remoteform" >
        <div class="form-group">
            <label class="col-sm-4 control-label" >
                Calcule economia média da cotação
            </label>
            <div class="col-sm-4" >
                <g:select from="${Cotacao.findAllByCodigoEstadoCotacaoBetween(4,8).findAll{def cot->cot.codigoEstadoCotacao!=EstadoCotacao.CANCELADA}.sort()}" noSelection="['':'-Escolha a Cotacao-']"
                          optionKey="id" name="idCotacao" class="form-control"/>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-4 col-sm-10">

                <button type="submit" class="btn btn-default">Enviar</button>
            </div>
        </div>
    </g:form>

    <hr>

    <g:form action="calculeFaltasEEconomiaAoAdicionarFornecedores" method="post">
        <div class="form-group">
            <label class="col-sm-4 control-label" >
                Calcule faltas e economia ao adicionar fornecedores com menor mix
            </label>
            <div class="col-sm-4" >
                <g:select from="${Cotacao.findAllByCodigoEstadoCotacaoBetween(4,8).findAll{def cot->cot.codigoEstadoCotacao!=EstadoCotacao.CANCELADA}.sort()}" noSelection="['':'-Escolha a Cotacao-']"
                          optionKey="id" name="idCotacao" class="form-control"/>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-4 col-sm-10">
                <button type="submit" class="btn btn-default">Enviar</button>
            </div>
        </div>
    </g:form>

    <hr>

    <g:form action="calculeEcomomiaAoAdicionarReps" method="post">
        <div class="form-group">
            <label class="col-sm-4 control-label" >
                Calcule número de opções de compra e faltas
            </label>
            <div class="col-sm-4" >
                <g:select from="${Cotacao.findAllByCodigoEstadoCotacaoBetween(4,8).findAll{def cot->cot.codigoEstadoCotacao!=EstadoCotacao.CANCELADA}.sort()}" noSelection="['':'-Escolha a Cotacao-']"
                          optionKey="id" name="idCotacao" class="form-control"/>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-4 col-sm-10">
                <button type="submit" class="btn btn-default">Enviar</button>
            </div>
        </div>
    </g:form>

</div>

</body>
</html>