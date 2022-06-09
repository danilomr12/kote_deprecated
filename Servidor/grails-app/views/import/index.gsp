<html>
<head><title>Importar planilha</title></head>
<body>
<g:form action="enviar" method="post"
        enctype="multipart/form-data">
  <input type="file" name="arquivo"/>
  <input type="submit"/>
</g:form>
<g:link action="downloadPlanilhaExemplo" controller="import">Download Planilha de Exemplo de Importação</g:link>
<g:if test="${flash.message}">
  <div class="message">${flash.message}</div>
</g:if>
</body>
</html>