<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title><g:layoutTitle default="Kote"></g:layoutTitle></title>


    <link href="${resource(dir:"/css", file: "bootstrap.css")}" rel="stylesheet">
    <link href="${resource(dir:"/css", file: "interface.css")}" rel="stylesheet" type="text/css">
    <link href="${resource(dir:"/css", file: "font-awesome.css")}" rel="stylesheet">

    <script src="${resource(dir:"/js", file: "jquery-latest.js")}"></script>
    <script src="${resource(dir:"/js", file: "bootstrap.min.js")}"></script>

    <!-- Begin Inspectlet Embed Code -->
    <script type="text/javascript" id="inspectletjs">
        var __insp = __insp || [];
        __insp.push(['wid', 725844271]);
        (function() {
            var a = window.onload;
            function __ldinsp(){var insp = document.createElement('script'); insp.type = 'text/javascript'; insp.async = true; insp.id = "inspsync"; insp.src = ('https:' == document.location.protocol ? 'https' : 'http') + '://www.inspectlet.com/inspect/725844271.js'; document.getElementsByTagName("head")[0].appendChild(insp); }
            window.onload = (typeof window.onload != 'function') ? __ldinsp : function() { a(); __ldinsp(); };
        })();
    </script>
    <!-- End Inspectlet Embed Code -->

    <g:layoutHead/>
</head>

<body>
<div class="container">

    <g:render template="/templates/topo"/>
    <!----------------------------------
	CONTEÚDO
------------------------------------>
    <div class="conteudo">

        <div class="account-container">

            <g:pageProperty name="page.middle"/>

        </div> <!-- /account-container -->

    </div><!--fim conteudo-->


<g:render template="/templates/contato" />
<g:render template="/templates/rodape" />


</div><!--Container-->
<g:layoutBody/>
</body>
</html>