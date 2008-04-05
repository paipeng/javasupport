<jsp:directive.include file="../taglibs.jsp"/>
<div class="content">
	<p>
	<a href="\${pageContext.request.contextPath}/webapp/${classNamePath}/list">Back to ${className} Listing.</a>
	</p>
	
	<h1>${className} ID \${${beanName}.id}</h1>
	<table>
	<% for (field in displayFields) { %>	
		<tr><td>${field[2]}</td><td>\${${beanName}.${field[0]}}</td></tr>
	<% } %>
	</table>
</div>