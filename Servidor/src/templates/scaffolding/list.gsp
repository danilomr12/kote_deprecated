<% import org.codehaus.groovy.grails.orm.hibernate.support.ClosureEventTriggeringInterceptor as Events %>
<%=packageName%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main" />
    <title>Listar ${className}</title>
</head>
<body>

<content tag="navbuttons">
    <li>
        <g:link class="create" action="create">Novo ${className}</g:link>
    </li>
    <g:form class="navbar-form navbar-right" action="buscar">
        <g:textField class="form-control" placeholder="Buscar ${className}" name="busca" title="buscar ${className}" style="width: 300px" />
        <g:submitButton name="Buscar" class="btn btn-default"/>
    </g:form>
</content>


<h1 class="page-header">Listar ${className}</h1>
<g:if test="\${flash.message}">
    <div class="alert alert-info">\${flash.message}</div>
</g:if>

<table class="table table-hover">
    <thead>
    <tr>
        <%
            excludedProps = ['version',
                    Events.ONLOAD_EVENT,
                    Events.BEFORE_DELETE_EVENT,
                    Events.BEFORE_INSERT_EVENT,
                    Events.BEFORE_UPDATE_EVENT]

            props = domainClass.properties.findAll { !excludedProps.contains(it.name) && it.type != Set.class }
            Collections.sort(props, comparator.constructors[0].newInstance([domainClass] as Object[]))
            props.eachWithIndex { p,i ->
                if(i < 6) {
                    if(p.isAssociation()) { %>
        <th>${p.naturalName}</th>
        <%          } else { %>
        <g:sortableColumn property="${p.name}" title="${p.naturalName}" />
        <%  }   }   } %>
    </tr>
    </thead>
    <tbody>
    <g:each in="\${${propertyName}List}" status="i" var="${propertyName}">
        <tr>
            <%  props.eachWithIndex { p,i ->
                if(i == 0) { %>
            <td><g:link action="show" id="\${${propertyName}.id}">\${fieldValue(bean:${propertyName}, field:'${p.name}')}</g:link></td>
            <%      } else if(i < 6) { %>
            <td>\${fieldValue(bean:${propertyName}, field:'${p.name}')}</td>
            <%  }   } %>
        </tr>
    </g:each>
    </tbody>
</table>
<div class="paginateButtons">
    <g:paginate total="${empresaInstanceTotal}" params="\${[busca: busca]}"/>
</div>
</body>
</html>
