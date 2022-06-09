<html>
    <head>
        <title>Kote</title>
		<meta name="layout" content="header" />
    </head>
    <body>
        <h1 style="margin-left:20px;">Controladores</h1>
        <div class="dialog" style="margin-left:20px;width:60%;">
            <ul>
              <g:each var="c" in="${grailsApplication.controllerClasses}">
                    <li class="controller"><g:link controller="${c.logicalPropertyName}">${c.fullName}</g:link></li>
              </g:each>
            </ul>
        </div>
    </body>
</html>