<jsp:directive.include file="../taglibs.jsp"/>
<div class="content">
	<h1>${className} ID \${${beanName}.id}</h1>
	<table>
	<% for (field in displayFields) { %>	
		<tr><td>${field[2]}</td><td>\${${beanName}.${field[0]}}</td></tr>
	<% } %>
	</table>
</div>