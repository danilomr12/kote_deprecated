<% import org.codehaus.groovy.grails.orm.hibernate.support.ClosureEventTriggeringInterceptor as Events %>
<%=packageName%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main" />
    <title>Create ${className}</title>
</head>
<body>
<content tag="navbuttons">
    <li>
        <g:link class="list" action="list">Listar ${className}</g:link>
    </li>
</content>

<div>
    <h1>Criar ${className}</h1>
    <g:if test="\${flash.message}">
        <div class="alert alert-info">\${flash.message}</div>
    </g:if>
    <g:hasErrors bean="\${${propertyName}}">
        <div class="alert alert-error">
            <g:renderErrors bean="\${${propertyName}}" as="list" />
        </div>
    </g:hasErrors>
    <g:form action="save" method="post" role="form" class="col-md-5" <%= multiPart ? ' enctype="multipart/form-data"' : '' %>>
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
                if(!Collection.class.isAssignableFrom(p.type)) {
                    cp = domainClass.constrainedProperties[p.name]
                    display = (cp ? cp.display : true)
                    if(display) { %>
        <div class="form-group \${hasErrors(bean:${propertyName},field:'${p.name}','has-error')}">
            <label for="${p.name}">${p.naturalName}:</label>
            ${renderEditor(p)}
        </div>


        <%  }   }   } %>

        <button type="submit" class="btn btn-default">Salvar</button>
    </g:form>
</div>
</body>
</html>
