<jsp:directive.include file="../taglibs.jsp"/>
<div class="content">

	<p>
	<a href="\${pageContext.request.contextPath}/webapp/${classNamePath}/create">Create New ${className}</a>
	</p>
	
	<h1>Listing of ${className}</h1>
	
	<table>
	
		<tr>
			<td>ID</td>
			<td>Actions</td>
		</tr>	
		
		<c:forEach var="${beanName}" items="\${${beanName}List}">
		<tr>
			<td>\${${beanName}.id}</td>
			<td>
			<a href="\${pageContext.request.contextPath}/webapp/${classNamePath}/show?id=\${${beanName}.id}">Show</a> |
			<a href="\${pageContext.request.contextPath}/webapp/${classNamePath}/edit?id=\${${beanName}.id}">Edit</a> |
			<a href="\${pageContext.request.contextPath}/webapp/${classNamePath}/delete?id=\${${beanName}.id}">Delete</a>
			</td>
		</tr>	
		</c:forEach>
	</table>
</div>