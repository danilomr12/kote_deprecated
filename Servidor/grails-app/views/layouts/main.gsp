<%@ page import="br.com.cotecom.domain.usuarios.empresa.Cliente; br.com.cotecom.services.UsuarioService; br.com.cotecom.domain.usuarios.Comprador" contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <title><g:layoutTitle default="KOTE admin" /></title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="">
        <meta name="author" content="Danilo">

        <link href="${resource(dir:"/css", file: "bootstrap2.css")}" rel="stylesheet">
        <link href="${resource(dir:"/css", file: "bootstrap_admin_extra.css")}" rel="stylesheet">
        <link href="${resource(dir:"/css", file: "dashboard.css")}" rel="stylesheet">
        <script src="../../assets/js/ie8-responsive-file-warning.js"></script>

        <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />

        <g:javascript library="application" />
        <script src="${resource(dir:"/js", file: "jquery-latest.js")}"></script>
        %{--<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>--}%
        <script src="${resource(dir:"/js", file: "bootstrap.js")}"></script>
        <!-- Just for debugging purposes. Don't actually copy this line! -->
        <!--[if lt IE 9]><script src="${resource(dir:"/js", file: "ie8-responsive-file-warning.js")}"></script><![endif]-->
        <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
        <![endif]-->
        <script src="${resource(dir:"/js",file:"docs.min.js")}"></script>
        <g:layoutHead />
    </head>
    <body>
    <div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
        <div class="container-fluid">
            <div class="navbar-header col-sm-3 col-md-2">
                <g:link url="http://www.kote.com.br" class="navbar-brand">KOTE</g:link>
            </div>
            <div class="navbar-collapse collapse">
                <ul class="nav navbar-nav navbar-left">
                    <li>
                        <g:link action="index" controller="admin">Home</g:link>
                    </li>
                    <g:pageProperty name="page.navbuttons"/>
                </ul>
                <ul class="nav navbar-nav navbar-right">
                    <li>
                        <g:link url="#">
                            <g:sayHelloToUser/>
                        </g:link>
                    </li>
                    <li>
                        <g:link controller="logout" action="index">Sair</g:link>
                    </li>

                    <sec:ifSwitched>
                        <li>
                            <a href='${request.contextPath}/j_spring_security_exit_user'>
                                Resume as <sec:switchedUserOriginalUsername/>
                            </a>
                        </li>
                    </sec:ifSwitched>
                </ul>
            </div>
        </div>
    </div>


    <div class="col-sm-3 col-md-2 sidebar">
        <ul class="nav nav-sidebar">
            <li class="${(empresaInstanceList||empresaInstance)?"active":""}"><g:link controller="empresaAdmin"> Empresas</g:link></li>
            <li class="${(usuarioInstanceList||usuarioInstance)?"active":""}"><g:link controller="usuarioAdmin"> Usuários</g:link></li>
            <li class="${(cotacaoInstanceList||cotacaoInstance)?"active":""}<g:pageProperty name="page.cotacaooactive"/>"><g:link controller="cotacaoAdmin"> Cotações</g:link></li>
            <li class="${(respostaInstanceList||respostaInstance)?"active":""}"><g:link controller="respostaAdmin"> Respostas</g:link></li>
            <li class="${(pedidoInstanceList||pedidoInstance)?"active":""}"><g:link controller="pedidoAdmin"> Pedidos</g:link></li>
            <li class="${(produtoInstanceList||produtoInstance)?"active":""}<g:pageProperty name="page.catalogoactive"/>"><g:link controller="catalogoAdmin"> Catálogo</g:link></li>
            <li class="${(produtoInstanceList||produtoInstance)?"active":""}<g:pageProperty name="page.testactive"/>"><g:link controller="test"> Teste</g:link></li>
        </ul>

    </div>

    <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <g:layoutBody />
    </div>
    </body>
</html>