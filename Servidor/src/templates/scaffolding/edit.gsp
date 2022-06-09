<% import org.codehaus.groovy.grails.orm.hibernate.support.ClosureEventTriggeringInterceptor as Events %>
<%=packageName%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main" />
    <title>Editar ${className}</title>
</head>
<body>
<content tag="navbuttons">
    <li>
        <g:link action="list">Listar ${className}s</g:link>
    </li>
    <li>
        <g:link action="create">Novo ${className}</g:link>
    </li>
</content>

<div>
    <h1 class="page-header">Editar ${className}</h1>
    <g:if test="\${flash.message}">
        <div class="alert alert-info">\${flash.message}</div>
    </g:if>
    <g:hasErrors bean="\${${propertyName}}">
        <div class="alert alert-error">
            <g:renderErrors bean="\${${propertyName}}" as="list" />
        </div>
    </g:hasErrors>

    <g:form role="form" class="col-md-6" method="post" <%= multiPart ? ' enctype="multipart/form-data"' : '' %>>
    <input type="hidden" name="id" value="\${${propertyName}?.id}" />
    <input type="hidden" name="version" value="\${${propertyName}?.version}" />
    <%
        excludedProps = ['version',
                'id',
                Events.ONLOAD_EVENT,
                Events.BEFORE_DELETE_EVENT,
                Events.BEFORE_INSERT_EVENT,
                Events.BEFORE_UPDATE_EVENT]
        props = domainClass.properties.findAll { !excludedProps.contains(it.name) }

        Collections.sort(props, comparator.constructors[0].newInstance([domainClass] as Object[]))
        props.each { p ->
            cp = domainClass.constrainedProperties[p.name]
            display = (cp ? cp.display : true)
            if(display) { %>
    <div class="form-group \${hasErrors(bean:${propertyName},field:'${p.name}','has-error')}">
        <label for="${p.name}">${p.naturalName}:</label>
        ${renderEditor(p)}
    </div>

    <%  }   } %>

    <g:actionSubmit action="update" class="save btn btn-default" value="Atualizar" />
    <g:actionSubmit action="delete" class="delete btn btn-default" onclick="return confirm('Are you sure?');" value="Deletear" />
    </g:form>
</div>
</body>
</html>
