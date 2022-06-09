<%@ page import="br.com.cotecom.domain.usuarios.empresa.Fornecedor; br.com.cotecom.domain.usuarios.empresa.CoteCom; br.com.cotecom.domain.usuarios.empresa.Cliente; br.com.cotecom.domain.usuarios.Usuario" %>
<%def list = [Usuario.ADMINISTRADOR, Usuario.REPRESENTANTE, Usuario.SUPERVISOR, Usuario.ESTOQUISTA, Usuario.COMPRADOR]%>
<%
    def clientList = Cliente.list().collect{"<option value="+it.id+">"+it.nomeFantasia+"</option>"}.join("").concat("<option value=''></option>");
    def adminList = CoteCom.list().collect{"<option value="+it.id+">"+it.nomeFantasia+"</option>"}.join("").concat("<option value=''></option>")
    def fornecedorList = Fornecedor.list().collect{"<option value="+it.id+">"+it.nomeFantasia+"</option>"}.join("").concat("<option value=''></option>")
    %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main" />
    <title>Novo Usuario</title>


    <script type="text/javascript" language="JavaScript">
        var clientes = "${clientList}";
        var fornecedores = "${fornecedorList}";
        var admin = "${adminList}";
        var $empresas = $("#empresas");
        var empId = ${usuarioInstance?.empresa?.id}
        $(document).ready(function(){
            $("#tipo").change(handler);
            handler();
            $("#empresas").val(empId);
        });
        function handler(){
            var $empresas = $("#empresas");
            switch($("#tipo").val()){
                case "${Usuario.ADMINISTRADOR}":
                    $empresas.empty();
                    $empresas.append(admin);
                    break;
                case "${Usuario.ESTOQUISTA}":
                    $empresas.empty();
                    $empresas.append(clientes);
                    break;
                case "${Usuario.COMPRADOR}":
                    $empresas.empty();
                    $empresas.append(clientes);
                    break;
                case "${Usuario.REPRESENTANTE}":
                    $empresas.empty();
                    $empresas.append(fornecedores);
                    break;
                case "${Usuario.SUPERVISOR}":
                    $empresas.empty();
                    $empresas.append(fornecedores);
                    break;
                default:
                    $empresas.append(admin);
            }
        }
    </script>
</head>
<body>
<content tag="navbuttons">
    <li>
        <g:link class="list" action="list">Listar Usuário</g:link>
    </li>
</content>

<div>
    <h1 class="page-header">Novo Usuario</h1>
    <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${usuarioInstance}">
        <div class="errors">
            <g:renderErrors bean="${usuarioInstance}" as="list" />
        </div>
    </g:hasErrors>
    <g:form action="save" method="post" role="form" class="col-md-5">

        <div class="form-group">
            <label for="tipo">Tipo:</label>
            <g:select id="tipo" from="${list}" name="tipo" class="form-control" value="${tipo}" noSelection="['null':'']"/>
        </div>

        <div class="form-group">
            <label for="email">Email:</label>
            <input class="form-control" type="text" id="email" name="email" value="${fieldValue(bean:usuarioInstance,field:'email')}"/>
        </div>
        <div class="form-group">
            <label for="nome">Nome:</label>
            <input type="text" id="nome" name="nome" class="form-control" value="${fieldValue(bean:usuarioInstance,field:'nome')}"/>
        </div>
        <div class="form-group">
            <label for="password">Password:</label>
            <input type="password" maxlength="255" id="password" name="password" class="form-control" value="${fieldValue(bean:usuarioInstance,field:'password')}"/>
        </div>
        <div class="form-group">
            <label for="empresa.id">Empresa:</label>
            <g:select id="empresas" optionValue="nomeFantasia" optionKey="id" from="${empresaList}" name="empresa.id" class="form-control" value="${usuarioInstance?.empresa?.id}" noSelection="['null':'']"/>
        </div>
        <div class="form-group">
            <label for="accountExpired">Account Expired:</label>
            <g:checkBox name="accountExpired" class="form-control" value="${usuarioInstance?.accountExpired}" ></g:checkBox>
        </div>
        <div class="form-group">
            <label for="accountLocked">Account Locked:</label>
            <g:checkBox name="accountLocked" class="form-control" value="${usuarioInstance?.accountLocked}" ></g:checkBox>
        </div>
        <div class="form-group">
            <label for="habilitado">Habilitado:</label>
            <g:checkBox name="habilitado" class="form-control" value="${usuarioInstance?.habilitado}" ></g:checkBox>
        </div>
        <div class="form-group">
            <label for="isDeletado">Is Deletado:</label>
            <g:checkBox name="isDeletado" class="form-control" value="${usuarioInstance?.isDeletado}" ></g:checkBox>
        </div>
        <div class="form-group">
            <label for="newUser">New User:</label>
            <g:checkBox name="newUser" class="form-control" value="${usuarioInstance?.newUser}" ></g:checkBox>
        </div>
        <div class="form-group">
            <label for="passwordExpired">Password Expired:</label>
            <g:checkBox name="passwordExpired" class="form-control" value="${usuarioInstance?.passwordExpired}" ></g:checkBox>
        </div>

        <div>
            <button type="submit" class="btn btn-default">Salvar</button>
        </div>
    </g:form>
</div>
</body>
</html>
