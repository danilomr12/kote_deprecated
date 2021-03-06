<% import org.codehaus.groovy.grails.orm.hibernate.support.ClosureEventTriggeringInterceptor as Events %>
<%=packageName%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main" />
    <title>Mostar ${className}</title>
</head>
<body>
<content tag="navbuttons">
    <li>
        <g:link class="list" action="list">Listar ${className}</g:link>
    </li>
    <li>
        <g:link class="create" action="create">Novo ${className}</g:link>
    </li>

</content>

<h1 class="page-header">Mostrar ${className}</h1>
<g:if test="\${flash.message}">
    <div class="alert alert-info">\${flash.message}</div>
</g:if>
<div class="col-md-8">
    <table class="table table-bordered table-striped">
        <tbody>

        <%
            excludedProps = ['version',
                    Events.ONLOAD_EVENT,
                    Events.BEFORE_DELETE_EVENT,
                    Events.BEFORE_INSERT_EVENT,
                    Events.BEFORE_UPDATE_EVENT]
            props = domainClass.properties.findAll { !excludedProps.contains(it.name) }
            Collections.sort(props, comparator.constructors[0].newInstance([domainClass] as Object[]))
            props.each { p -> %>
        <tr class="prop">
            <td valign="top" class="name">${p.naturalName}:</td>
            <% if(p.isEnum()) { %>
            <td valign="top" class="value">\${${propertyName}?.${p.name}?.encodeAsHTML()}</td>
            <% } else if(p.oneToMany || p.manyToMany) { %>
            <td  valign="top" style="text-align:left;" class="value">
                <ul>
                    <g:each var="${p.name[0]}" in="\${${propertyName}.${p.name}}">
                        <li><g:link controller="${p.referencedDomainClass?.propertyName}" action="show" id="\${${p.name[0]}.id}">\${${p.name[0]}?.encodeAsHTML()}</g:link></li>
                    </g:each>
                </ul>
            </td>
            <%  } else if(p.manyToOne || p.oneToOne) { %>
            <td valign="top" class="value"><g:link controller="${p.referencedDomainClass?.propertyName}" action="show" id="\${${propertyName}?.${p.name}?.id}">\${${propertyName}?.${p.name}?.encodeAsHTML()}</g:link></td>
            <%  } else  { %>
            <td valign="top" class="value">\${fieldValue(bean:${propertyName}, field:'${p.name}')}</td>
            <%  } %>
        </tr>
        <%  } %>
        </tbody>
    </table>
    <g:form>
        <input type="hidden" name="id" value="\${${propertyName}?.id}" />
        <g:actionSubmit class="edit btn btn-default" action="edit" value="Editar" />
        <g:actionSubmit class="delete btn btn-default" action="delete" onclick="return confirm('Tem certeza?');" value="Deletar" />
    </g:form>
</div>
</body>
</html>
